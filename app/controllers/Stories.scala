package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json._
import play.Logger

import views._
import models._

object Stories extends Controller {
  
  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File(s"public/images/uploads/$filename"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.Application.index()).flashing(
        "error" -> "Missing file")
    }
  }

  def buildJson(seriesStoryPages: Seq[(Series, Story, Page)]) = {
    Logger.debug(s"Series story pages size:${seriesStoryPages.size}")
    
    val jsPages = seriesStoryPages.foldLeft(Seq[JsObject]())((res, rec) => {
        rec match {
          case ((series, story, page)) => {
            res :+ Json.obj(
              "title" -> JsString(story.title),
              "image" -> JsString(s"/assets/images/${page.imagePath}"),
              "summary" -> JsString(story.summary),
              "series" -> Json.obj(
                "id" -> JsNumber(series.id.get),
                "name" -> JsString(series.name)
              )
            )
          }
          case _ => res
        }
      }   
    )
    
    Json.obj("pages" ->  JsArray(jsPages))
  }

  def findCoverPages = DBAction { implicit rs =>
    Logger.debug(s"In findCoverPages.search")
    Ok(buildJson(PageDAO.listCovers))
  }
  
  def findCoverPagesBySeries(id: Long) = DBAction { implicit rs =>
    Logger.debug(s"In Stories.findCoverPagesBySeries for id:${id}")
    
    Ok(buildJson(PageDAO.listCoversBySeries(id)))
  }

}

