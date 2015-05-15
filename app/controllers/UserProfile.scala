package controllers

import play.api._
import play.api.mvc._
import views._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import securesocial.core._
import models.BasicUser

class UserProfile(override implicit val env: RuntimeEnvironment[BasicUser]) extends securesocial.core.SecureSocial[BasicUser] {
  
  def index = UserAwareAction { implicit request =>
    val username = request.user match {
      case Some(user) => user.main.fullName
      case _ => None
    }
    Ok(views.html.user.index(username))
  }

}