package com.jpreddy.graphmodelling

import scala.collection.immutable.SortedMap
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object OrganizationStructureCreationApp extends App {
  
  val logger= LoggerFactory.getLogger("com.jpreddy.graphmodelling")
   
  logger.debug("Starting the Application")
//  Q1.1 Create a data model to represent this structure.
  //Labels
  val organizationLabel =Label("ORGANIZATION")
  val employeeLabel =Label("EMPLOYEE")
  val projectLabel = Label("PROJECT")
  
  //RelationshipTypes
  val employedWith = RelationshipType("EMPLOYED_WITH")
  val reportsTo = RelationshipType("REPORTS_TO")
  val worksOn = RelationshipType("WORKS_ON")
  
  //Nodes
  val company =Node(organizationLabel,Map("name" -> "NEWSCORP"), Set())
  /*John works for Lisa and works on Project A and Project B */
  val john = Node(employeeLabel, Map("name" -> "JOHN"), Set())
  val lisa =  Node(employeeLabel, Map("name" -> "LISA"), Set())
  val projectA = Node(projectLabel, Map("name" -> "A"), Set())
  val projectB = Node(projectLabel, Map("name" -> "B"), Set())
  val projectC = Node(projectLabel, Map("name" -> "C"), Set())
  val johnReportsTo = Relationship(reportsTo,john,lisa)
  val johnWorksOn = Relationship(worksOn, john, projectA)
  val johnEmployedWith = Relationship(employedWith,john,company)
  
  //  Jack also works for Lisa and works on Project A
  val jack = Node(employeeLabel, Map("name" -> "JACK"), Set())
  val jackReportsTo = Relationship(reportsTo,jack,lisa)
  val jackWorksOn = Relationship(worksOn, jack, projectA)
  val jackEmployedWith = Relationship(employedWith,jack,company)
  
 
  
  //James works for Leonard and works on Project A and Project C
  val james = Node(employeeLabel, Map("name" -> "JAMES"), Set())
  val leonard = Node(employeeLabel, Map("name" -> "LEONARD"), Set())
  val jamesReportsTo = Relationship(reportsTo,james,leonard)
  val jamesWorksOn1 = Relationship(worksOn, james, projectA)
  val jamesWorksOn2 = Relationship(worksOn, james, projectC)
  val jamesEmployedWith = Relationship(employedWith,james,company)
  //Lucy works for Leonard and works on Project B and Project C
  val lucy = Node(employeeLabel, Map("name" -> "LUCY"), Set())
  val lucyReportsTo = Relationship(reportsTo,lucy,leonard)
  val lucyWorksOn1 = Relationship(worksOn, lucy, projectB)
  val lucyWorksOn2 = Relationship(worksOn, lucy, projectC)
  val lucyEmployedWith = Relationship(employedWith,lucy,company)
  //Sam works for Leonard and works on Project B
  val sam = Node(employeeLabel, Map("name" -> "SAM"), Set())
  val samReportsTo = Relationship(reportsTo,sam,leonard)
  val samWorksOn1 = Relationship(worksOn, sam, projectB)
  val samEmployedWith = Relationship(employedWith,sam,company)
  //Lisa and Leonard work for Simon
  val simon = Node(employeeLabel,Map("name" -> "SIMON"), Set()) 
  val simonEmployedWith = Relationship(employedWith,simon,company)
  val lisaReportsTo = Relationship(reportsTo, lisa, simon)
  val lisaEmployedWith = Relationship(employedWith,lisa,company)
  val leonardReportsTo = Relationship(reportsTo, leonard, simon)
  val leonardEmployedWith = Relationship(employedWith,leonard,company)
  
   logger.debug("*** Q1.1 : Organization Structure ***")
   logger.debug("Organization, Employees and Projects are represented as Nodes.Their relationships,propeties and labels together form a graph structure.")
   logger.debug("Hence Graph data model is chosen to represent the strucure.")
   logger.debug("Refer source code for the details of the data model")

  val jamesTeammates =getTeamMates(company,james)
  logger.debug("")
  logger.debug("*** Q1.2 : JAMES TEAM MATES ***")
  printNodes(jamesTeammates)
  
  val FCM =getFirstCommonManager(john, sam)
  logger.debug("")
  logger.debug("*** Q1.3 :First common manager of John and Sam ***")
  logger.debug(FCM.getOrElse(company).props.getOrElse("name","NoName").asInstanceOf[String])
  
  val managerOfProjectA = findPeopleManager(projectA)
  logger.debug("")
  logger.debug("*** Q1.4 : Manager of project A ***")
  logger.debug(managerOfProjectA.toString)
  
  logger.debug("Ending the Application")
  
  
//   Q1.2 Create a function that for a given organisation and employee will return all their project
//team mates (employees that work on a common project).
   
  def getTeamMates(org :Node, emp:Node) :Set[Node] =
  {
    val worksOn = RelationshipType("WORKS_ON")
    val allProjs = getRelatedToNodes(emp, worksOn)
    allProjs.flatMap( x => getRelatedFromNodes(x,worksOn)) - emp
    
  }

//  Q1.3 Create a function that for a given organisation and two employees will return their first
//common manager.
  def getFirstCommonManager(emp1:Node, emp2:Node) :Option[Node] =
  {
    val reportsTo =RelationshipType("REPORTS_TO")
    val managerSet1= getManagers(emp1)
      def findFirstCommonManager(emp: Node,managers: Set[Node]): Option[Node]=
      {
         if(managers.contains(emp)) Some(emp)
         else{
           val managerSet= getRelatedToNodes(emp, reportsTo)
           if(managerSet.isEmpty) None
           else
             findFirstCommonManager(managerSet.head, managers)
         }
        
      }
    
    findFirstCommonManager(emp2,managerSet1)
    
  }
  
//  Q2.4 Create a function that for a given organisation and project will provide a guess on the
//closest people manager responsible for the project based on the number of direct and
//indirect reports working on it.
  
  def findPeopleManager(project : Node):Option[Node]=
  {
    val worksOn=RelationshipType("WORKS_ON")
    val reportsTo =RelationshipType("REPORTS_TO")
    val projectMates= getRelatedFromNodes(project, worksOn)
    val managers=projectMates.toList.flatMap( x => getRelatedToNodes(x, reportsTo))
    val sortedMap = createSortedMap(managers)
    val managerEntry =sortedMap.last
    if(managerEntry._1 == null) None
    else Some(managerEntry._1)
    
  }
  
  def getRelationships(node:Node, rType : RelationshipType) :Set[Relationship] =
  {
    node.relationships.filter(r => r.relationshipType.name == rType.name ).toSet
  }
  
   def getRelatedToNodes(node:Node, rType : RelationshipType) :Set[Node] =
  {
    node.relationships
    .filter(r => r.relationshipType.name == rType.name )
    .filter(r => r.toNode.props.getOrElse("name","NoName") != node.props.getOrElse("name","NoName"))
    .map(x => x.toNode)
  }
   
   def getRelatedFromNodes(node:Node, rType : RelationshipType) :Set[Node] =
  {
    node.relationships
    .filter(r => r.relationshipType.name == rType.name )
    .filter(r => r.fromNode.props.getOrElse("name","NoName") != node.props.getOrElse("name","NoName"))
    .map(x => x.fromNode)
  }
   
    def getManagers(emp:Node) : Set[Node] =
  {
    val reportsTo =RelationshipType("REPORTS_TO")
    def getManager(emp:Node, managers:Set[Node]): Set[Node]=
    {
       val managerSet= getRelatedToNodes(emp, reportsTo)
       if(managerSet.isEmpty)
         managers
       else getManager(managerSet.head, managers + managerSet.head)
     }
    
      getManager(emp, Set[Node]())
   
  }
   
  def createSortedMap(managers: List[Node]) :scala.collection.immutable.SortedMap[Node,Int]=
  {
    var managersMap = scala.collection.immutable.SortedMap[Node,Int]()
    managers.map{ manager=>
      val count =managersMap.getOrElse(manager, 0)
      val cnt = count + 1
      managersMap += (manager -> cnt)
      
      }
    managersMap
  }
  def printNodes(nodes :Set[Node]) =
  {
    nodes.foreach(x => logger.debug(x.props.getOrElse("name","NoName").asInstanceOf[String]))
  }
}