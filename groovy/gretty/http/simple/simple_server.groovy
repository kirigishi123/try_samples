/*
 * Gretty sample
 *   Groovy 1.8 で実行する必要あり
 *   Groovy 2.0 では ExceptionInitilizerError が発生する
 *
 */
@GrabResolver(name = 'gretty', root = 'http://groovypp.artifactoryonline.com/groovypp/libs-releases-local/')
@Grab('org.mbte.groovypp:gretty:0.4.302')
import org.mbte.gretty.httpserver.*

GrettyServer server = []

server.groovy = [
	localAddress: new InetSocketAddress('localhost', 8080),
	'/': {
		get {
			response.html = 'hello'
		}
	}
]

server.start()
