@Grab('com.foundationdb:fdb-sql-parser:1.4.0')
import com.foundationdb.sql.parser.*

def findTargetNodes = { node, cls ->
	def res = []

	node.accept(
		[
			visit: { n -> 
				if (cls.isInstance(n)) {
					res << n
				}
				n
			},
			skipChildren: { n -> false },
			stopTraversal: { -> false },
			visitChildrenFirst: { n -> false}
		] as Visitor
	)
	res
}

def parser = new SQLParser()

def node = parser.parseStatement(System.in.text)

def list = findTargetNodes(node, ValueNode)

list.each {
	println '------------------'

	println it.class
	println it
}
