
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import play.api._
import models._
import play.api.db.slick._
import play.api.Play.current
import controllers.{CustomRoutesService, AuthViewTemplates}
import java.lang.reflect.Constructor
import securesocial.core.RuntimeEnvironment
import service.DBUserService
import securesocial.core.services.{AuthenticatorService, UserService}
import service.DBUserService
import securesocial.controllers.ViewTemplates

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
  }
  
object StorytellersRuntimeEnvironment extends RuntimeEnvironment.Default[BasicUser] {
    override implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext
    override lazy val routes = new CustomRoutesService()
    override lazy val userService: DBUserService = new DBUserService()
//    override lazy val eventListeners = List(new MyEventListener())
    override lazy val viewTemplates: ViewTemplates = new AuthViewTemplates(this)
  }

  /**
   * An implementation that checks if the controller expects a RuntimeEnvironment and
   * passes the instance to it if required.
   *
   * This can be replaced by any DI framework to inject it differently.
   *
   * @param controllerClass
   * @tparam A
   * @return
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    val instance = controllerClass.getConstructors.find { c =>
      val params = c.getParameterTypes
      params.length == 1 && params(0) == classOf[RuntimeEnvironment[BasicUser]]
    }.map {
      _.asInstanceOf[Constructor[A]].newInstance(StorytellersRuntimeEnvironment)
    }
    instance.getOrElse(super.getControllerInstance(controllerClass))
  }
  
}

/** Initial set of data to be imported into the sample application. */
object InitialData {

  def insert(): Unit = {
    DB.withSession { implicit s: Session =>
        
      if (SeriesDAO.count == 0) {
          Seq(
            Series(Option(1L), "Random 1", None, DateTime.parse("2014-01-01")),
            Series(Option(2L), "Dilbert", None, DateTime.parse("2014-03-19")),
            Series(Option(3L), "Calvin and Hobbs", None, DateTime.parse("2014-06-21")),
            Series(Option(4L), "Garfield", None, DateTime.parse("2014-07-08")),
            Series(Option(5L), "The Farside", None, DateTime.parse("2014-11-05"))
        ).foreach(SeriesDAO.forceInsert)
      }
      
      if (StoryDAO.count == 0) {
        Seq(
          Story(Option(1L), "Comic Strip 1", """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed feugiat consectetur pellentesque. Nam acelit risus, ac blandit dui. 
            Duis rutrum porta tortor ut convallis. Duis rutrum porta tortor ut convallis.""", Some(1), DateTime.parse("2014-01-01")),
          Story(Option(2L), "Comic Strip 2", """
            Donec a fermentum nisi.""", Some(1), DateTime.parse("2014-09-14")),
          Story(Option(3L), "Comic Strip 3", """
            Nullam eget lectus augue. Donec eu sem sit amet ligula faucibus suscipit. Suspendisse rutrum turpis quis
            nunc convallis quis aliquam mauris suscipit.""", Some(2), DateTime.parse("2014-11-16")),
          Story(Option(4L), "Comic Strip 4", """
            Donec a fermentum nisi.""", Some(2), DateTime.parse("2014-03-21")),
          Story(Option(5L), "Comic Strip 5", """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed feugiat consectetur pellentesque. Nam acelit risus, ac blandit dui. 
            Duis rutrum porta tortor ut convallis. Duis rutrum porta tortor ut convallis.""", Some(2), DateTime.parse("2014-06-21")),
          Story(Option(6L), "Comic Strip 6", """
            Donec a fermentum nisi.""", Some(3), DateTime.parse("2014-02-04")),
          Story(Option(7L), "Comic Strip 7", """
            Nullam eget lectus augue. Donec eu sem sit amet ligula faucibus suscipit. Suspendisse rutrum turpis quis
            nunc convallis quis aliquam mauris suscipit.""", Some(3), DateTime.parse("2014-06-08")),
          Story(Option(8L), "Comic Strip 8", """
            Donec a fermentum nisi.""", Some(3), DateTime.parse("2014-06-04")),
          Story(Option(9L), "Comic Strip 9", """
            Nullam eget lectus augue. Donec eu sem sit amet ligula faucibus suscipit. Suspendisse rutrum turpis quis
            nunc convallis quis aliquam mauris suscipit.""", Some(4), DateTime.parse("2014-02-09")),
          Story(Option(10L), "Comic Strip 10", """
            Donec a fermentum nisi.""", Some(4), DateTime.parse("2014-09-11")),
          Story(Option(11L), "Comic Strip 11", """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed feugiat consectetur pellentesque. Nam acelit risus, ac blandit dui. 
            Duis rutrum porta tortor ut convallis. Duis rutrum porta tortor ut convallis.""", Some(4), DateTime.parse("2014-05-19")),
          Story(Option(12L), "Comic Strip 12", """
            Nullam eget lectus augue. Donec eu sem sit amet ligula faucibus suscipit. Suspendisse rutrum turpis quis
            nunc convallis quis aliquam mauris suscipit.""", Some(5), DateTime.parse("2014-12-21")),
          Story(Option(13L), "Comic Strip 13", """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed feugiat consectetur pellentesque. Nam acelit risus, ac blandit dui. 
            Duis rutrum porta tortor ut convallis. Duis rutrum porta tortor ut convallis.""", Some(5), DateTime.parse("2015-01-11")),
          Story(Option(14L), "Comic Strip 14", """
            Donec a fermentum nisi.""", Some(5), DateTime.parse("2015-02-13")),
          Story(Option(15L), "Comic Strip 15", """
            Nullam eget lectus augue. Donec eu sem sit amet ligula faucibus suscipit. Suspendisse rutrum turpis quis
            nunc convallis quis aliquam mauris suscipit.""", Some(5), DateTime.parse("2014-07-21"))
        ).foreach(StoryDAO.forceInsert)
      }
      
      if (PageDAO.count == 0) {
        Seq(
          Page(Option(1), Option(1), "uploads/cs1.png"),
          Page(Option(1), Option(2), "uploads/cs2.png"),
          Page(Option(1), Option(3), "uploads/cs3.png"),
          Page(Option(1), Option(4), "uploads/cs4.png"),
          Page(Option(1), Option(5), "uploads/cs5.png"),
          Page(Option(1), Option(6), "uploads/cs6.png"),
          Page(Option(1), Option(7), "uploads/cs7.png"),
          Page(Option(1), Option(8), "uploads/cs8.png"),
          Page(Option(1), Option(9), "uploads/cs9.png"),
          Page(Option(1), Option(10), "uploads/cs10.png"),
          Page(Option(1), Option(11), "uploads/cs11.png"),
          Page(Option(1), Option(12), "uploads/cs12.png"),
          Page(Option(1), Option(13), "uploads/cs13.png"),
          Page(Option(1), Option(14), "uploads/cs14.png"),
          Page(Option(1), Option(15), "uploads/cs15.png")
        ).foreach(PageDAO.forceInsert)
      }
 
    }
  }
}