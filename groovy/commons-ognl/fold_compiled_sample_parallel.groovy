@GrabResolver('http://repository.apache.org/snapshots/')
@Grab('org.apache.commons:commons-ognl:4.0-SNAPSHOT')
import org.apache.commons.ognl.Ognl
import org.apache.commons.ognl.ClassResolver

import groovy.transform.*
import groovyx.gpars.*

class Sample {
	def lines = []
}

@Immutable
class Item {
	int code
	BigDecimal price = BigDecimal.ZERO
}

def data = new Sample()
data.lines << new Item(code: 1, price: 100)
data.lines << new Item(code: 2, price: 200)
data.lines << new Item(code: 3, price: 300)

def ctx = Ognl.createDefaultContext(null, { String className, Map<String, Object> context ->
	Class.forName(className)
} as ClassResolver)

def expr1 = Ognl.compileExpression(ctx, null, '#v = 0, lines.{ #v = #v + #this.price }, #v')

def expr2 = Ognl.compileExpression(ctx, null, '#fold = :[ #this[2].size() > 0 ? #fold({ #this[0], #this[0]({ #this[1], #this[2][0] }), #this[2].subList(1, #this[2].size()) }) : #this[1] ], #fold({ :[ #this[0] + #this[1].price ], 0, lines })')

def printResult = {
	it.groupBy().each { k, v -> println "${k} ${v.size()}" }
}

def count = 50

GParsPool.withPool(20) {
	def res1 = (0..<count).collectParallel {
		// #v は Global スコープ変数のため問題が生じる
		try {
			Ognl.getValue('#v = 0, lines.{ #v = #v + #this.price }, #v', ctx, data)
		} catch (e) {
			null
		}
	}

	def res2 = (0..<count).collectParallel {
		Ognl.getValue(expr1, data)
	}

	def res3 = (0..<count).collectParallel {
		// #fold は Global スコープ変数のため問題が生じる
		try {
			Ognl.getValue('#fold = :[ #this[2].size() > 0 ? #fold({ #this[0], #this[0]({ #this[1], #this[2][0] }), #this[2].subList(1, #this[2].size()) }) : #this[1] ], #fold({ :[ #this[0] + #this[1].price ], 0, lines })', ctx, data)
		} catch (e) {
			null
		}
	}

	def res4 = (0..<count).collectParallel {
		Ognl.getValue(expr2, data)
	}

	println '----- res1 -----'
	printResult res1

	println '----- res2 -----'
	printResult res2

	println '----- res3 -----'
	printResult res3

	println '----- res4 -----'
	printResult res4
}
