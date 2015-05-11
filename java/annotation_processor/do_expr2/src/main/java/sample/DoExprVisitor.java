package sample;

import com.sun.source.tree.*;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class DoExprVisitor extends TreeScanner<Void, Void> {
	private static final String DO_TYPE = "$do";
	private static final String BIND_CODE = "${var}.bind(${rExpr}, ${lExpr} -> ${body})";
	private static final String UNIT_CODE = "${var}.unit( ${expr} )";

	private ParserFactory parserFactory;
	private Map<String, TemplateBuilder> builderMap = new HashMap<>();

	public DoExprVisitor(Context context) {
		parserFactory = ParserFactory.instance(context);

		builderMap.put("let", new TemplateBuilder(BIND_CODE, this::createBindParams));
		builderMap.put("return", new TemplateBuilder(UNIT_CODE, this::createUnitParams));
	}

	@Override
	public Void visitLambdaExpression(LambdaExpressionTree node, Void p) {
		if (node instanceof JCLambda) {
			JCLambda lm = (JCLambda)node;

			if (lm.params.size() == 1) {
				JCVariableDecl param = lm.params.get(0);

				if (isDoType(param)) {
					// ラムダの引数を消去
					lm.params = com.sun.tools.javac.util.List.nil();
					lm.paramKind = JCLambda.ParameterKind.EXPLICIT;

					JCExpression ne = parseExpression(createExpression((JCBlock)lm.body, createBaseParams(param)));
					adjustPos(ne, lm.pos);

					lm.body = ne;
				}
			}
		}
		return super.visitLambdaExpression(node, p);
	}

	// pos の値を調整する
	private void adjustPos(JCExpression ne, final int basePos) {
		ne.accept(new com.sun.tools.javac.tree.TreeScanner() {
			public void scan(JCTree tree) {
				if(tree!=null) {
					tree.pos += basePos;
					super.scan(tree);
				}
			}
		});
	}

	private String createExpression(JCBlock block, Map<String, String> params) {
		Stream<String> revExpr = block.stats.reverse().stream().map(s -> s.toString().replaceAll(";", ""));

		return revExpr.reduce("", (acc, v) -> {
			int spacePos = v.indexOf(" ");
			String action = v.substring(0, spacePos);

			if (builderMap.containsKey(action)) {
				acc = builderMap.get(action).build(params, acc, v.substring(spacePos + 1));
			}

			return acc;
		});
	}

	private JCExpression parseExpression(String doExpr) {
		return parserFactory.newParser(doExpr, false, false, false).parseExpression();
	}

	private boolean isDoType(JCVariableDecl param) {
		return param.name.toString().endsWith(DO_TYPE);
	}

	private Map<String, String> createBaseParams(JCVariableDecl param) {
		Map<String, String> params = new HashMap<>();

		params.put("var", param.name.toString().replace(DO_TYPE, ""));

		return params;
	}

	private Map<String, String> createBindParams(String body, String expr) {
		Map<String, String> params = createUnitParams(body, expr);

		String[] vexp = expr.split("=");
		params.put("lExpr", vexp[0]);
		params.put("rExpr", vexp[1]);

		return params;
	}

	private Map<String, String> createUnitParams(String body, String expr) {
		Map<String, String> params = new HashMap<>();

		params.put("body", body);
		params.put("expr", expr);

		return params;
	}

	private class TemplateBuilder {
		private static final String VAR_PREFIX = "\\$\\{";
		private static final String VAR_SUFFIX = "\\}";

		private String template;
		private BiFunction<String, String, Map<String, String>> paramCreator;

		TemplateBuilder(String template, BiFunction<String, String, Map<String, String>> paramCreator) {
			this.template = template;
			this.paramCreator = paramCreator;
		}

		public String build(Map<String, String> params, String body, String expr) {
			return buildTemplate(
					buildTemplate(template, params),
					paramCreator.apply(body, expr));
		}

		private String buildTemplate(String template, Map<String, String> params) {
			return params.entrySet().stream().reduce(template,
					(acc, v) -> acc.replaceAll(VAR_PREFIX + v.getKey() + VAR_SUFFIX, v.getValue()),
					(a, b) -> a);
		}
	}
}
