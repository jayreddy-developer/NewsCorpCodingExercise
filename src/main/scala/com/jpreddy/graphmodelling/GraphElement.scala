package com.jpreddy.graphmodelling

trait GraphElement
case class Label(name: String) extends GraphElement
case class RelationshipType(name:String)
case class Relationship(relationshipType: RelationshipType, fromNode:Node, toNode: Node) extends GraphElement{
fromNode.relationships += this
toNode.relationships +=  this

override def equals(o: Any) = o match {
    case that: Relationship => (that.relationshipType == this.relationshipType) && (that.fromNode == this.fromNode) && (that.toNode == this.toNode)
    case _ => false
  }
  override def hashCode = this.fromNode.hashCode() + this.toNode.hashCode()
}
case class Node(label:Label, props:Map[String,Any],var relationships: Set[Relationship]) extends GraphElement with Ordered[Node]
{
  override def equals(o: Any) = o match {
    case that: Node => (that.label == this.label) && (that.props.getOrElse("name","NoName") == this.props.getOrElse("name","NoName")) 
    case _ => false
  }
  override def hashCode = this.props.getOrElse("name","NoName").hashCode
  
  import scala.math.Ordered.orderingToOrdered

  def compare(that: Node): Int = this.props.getOrElse("name","NoName").asInstanceOf[String] compare (that.props.getOrElse("name","NoName").asInstanceOf[String])
  
  override def toString= {
    this.props.getOrElse("name","NoName").asInstanceOf[String]
  }

}

//case class Direction(dir:String)
//case object forwardDirection extends Direction("FORWARD")




