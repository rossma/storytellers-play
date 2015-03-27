package controllers

import play.api._
import play.api.mvc._
import views._
import play.api.libs.json.JsValue
import play.api.libs.json.Json

object Security extends Controller {

  def login = Action {
    Ok(views.html.login())
  }

}
