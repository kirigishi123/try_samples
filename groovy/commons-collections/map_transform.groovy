@Grab('commons-collections:commons-collections:3.2.1')
import org.apache.commons.collections.TransformerUtils
import org.apache.commons.collections.map.TransformedMap


def data = [
	id: 'test1',
	value: 10,
	date: new Date()
]

def tm = TransformedMap.decorateTransform(
	data, 
	TransformerUtils.mapTransformer(id: 'code', value: 'point', date: 'datestring'), 
	TransformerUtils.nopTransformer()
)

/* decorate の場合は既存データを変換しない

def tm = TransformedMap.decorate(
	data, 
	TransformerUtils.mapTransformer(id: 'code', value: 'point', date: 'datestring'), 
	TransformerUtils.nopTransformer()
)

tm['id'] = 'abc'
*/

println tm

