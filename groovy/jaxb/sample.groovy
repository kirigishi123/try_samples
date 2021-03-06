
import javax.xml.bind.JAXBContext
import javax.xml.bind.annotation.*

//groovy のクラスが邪魔するので @XmlAccessorType(XmlAccessType.NONE) を使う
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Customer {
	@XmlElement
	String id

	@XmlElement
	String name
}

def ctx = JAXBContext.newInstance(Customer.class)

ctx.createMarshaller().marshal(new Customer(id: "id:1", name: "test"), System.out)

