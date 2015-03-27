package controllers

import play.api._
import play.api.mvc._
import views._
import play.api.libs.json.JsValue
import play.api.libs.json.Json

import securesocial.core._
import service.DemoUser

class Application(override implicit val env: RuntimeEnvironment[DemoUser]) extends securesocial.core.SecureSocial[DemoUser] {
  
  def sindex = SecuredAction { implicit request =>
    Ok(views.html.sindex(request.user.main))
  }
  
  def index = UserAwareAction { implicit request =>
    val username = request.user match {
      case Some(user) => user.main.fullName
      case _ => None
    }
    Ok(views.html.index(username))
  }
  
//  // a sample action using an authorization implementation
//  def onlyTwitter = SecuredAction(WithProvider("twitter")) { implicit request =>
//    Ok("You can see this because you logged in using Twitter")
//  }
//
//  def linkResult = SecuredAction { implicit request =>
//    Ok(views.html.linkResult(request.user))
//  }
//
//  /**
//   * Sample use of SecureSocial.currentUser. Access the /current-user to test it
//   */
//  def currentUser = Action.async { implicit request =>
//    SecureSocial.currentUser[DemoUser].map { maybeUser =>
//      val userId = maybeUser.map(_.main.userId).getOrElse("unknown")
//      Ok(s"Your id is $userId")
//    }
//  }
}

// An Authorization implementation that only authorizes uses that logged in using twitter
//case class WithProvider(provider: String) extends Authorization[DemoUser] {
//  def isAuthorized(user: DemoUser, request: RequestHeader) = {
//    user.main.providerId == provider
//  }
//}

object Application extends Controller {
  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        Stories.findCoverPages,
        Stories.findCoverPagesBySeries
      )
    ).as("text/javascript")
  }

//  def index = Action {
//    Ok(views.html.index(Some("ddd")))
//  }
}