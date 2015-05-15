package controllers

import play.api._
import play.api.mvc._
import views._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import java.io.File
import securesocial.core._
import models.BasicUser

class StoryCreation(override implicit val env: RuntimeEnvironment[BasicUser]) extends securesocial.core.SecureSocial[BasicUser] {

  def index = UserAwareAction { implicit request =>
    val username = request.user match {
      case Some(user) => user.main.fullName
      case _ => None
    }
    Ok(views.html.create.index(username))
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File(s"public/images/uploads/$filename"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.StoryCreation.index).flashing(
        "error" -> "Missing file")
    }
  }
}

