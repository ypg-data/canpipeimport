package canpipe.xml

import util.Logging
import util.wrapper.Wrapper

trait Elem extends Wrapper[xml.Elem] {
  def value: xml.Elem
  def numChilds: Int = value.child.count(!_.isAtom)
}

object Elem extends Logging {

  import util.xml.Base._
  def apply(n: scala.xml.Elem): Option[Elem] = {
    val totalElems = n.child.count(!_.isAtom)
    val importantFields = List(
      (XMLFields.eventId.toString, (n \ XMLFields.eventId.asList).size),
      (XMLFields.eventTimestamp.toString, (n \ XMLFields.eventTimestamp.asList).size))
    importantFields.
      find { case (_, howMany) => howMany < totalElems }.
      map {
        case (fieldName, howMany) =>
          logger.error(s"${howMany} elements (out of ${totalElems}) have ${fieldName}")
          None
      }.getOrElse(Some(NodeSeqImpl(value = n)))
  }
  private case class NodeSeqImpl(value: xml.Elem) extends Elem
}

