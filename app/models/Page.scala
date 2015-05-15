package models

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag
import play.Logger

case class Page(sequence: Option[Int], storyId: Option[Long], imagePath: String, isCover: Boolean = true, 
    created: DateTime = new DateTime)

class PageTbl(tag: Tag) extends Table[Page](tag, "PAGE") {
  
  def sequence = column[Int]("SEQUENCE")
  def imagePath = column[String]("IMAGE_PATH", O.NotNull)
  def isCover = column[Boolean]("IS_COVER")
  def storyId = column[Long]("STORY_ID", O.NotNull)
  def created = column[DateTime]("CREATION_DATE", O.NotNull)
  def * = (sequence.?, storyId.?, imagePath, isCover, created) <> (Page.tupled, Page.unapply _)
  def pk = primaryKey("PAGE_PK", (sequence, storyId))
}

object PageDAO {
  val seriesQ = TableQuery[SeriesTbl]
  val stories = TableQuery[StoryTbl]
  val pages = TableQuery[PageTbl]

  def listByStory(storyId: Long)(implicit s: Session) = 
    pages.filter(_.storyId === storyId).list

  def count(implicit s: Session): Int =
    Query(pages.length).first

  def insert(page: Page)(implicit s: Session) {
    pages.insert(page)
  }

  /**
   * Insert a new page by ignoring auto inc column
   */
  def forceInsert(page: Page)(implicit s: Session) {
    pages.forceInsert(page)
  }

//  def list(implicit s: Session) = {
//    pages.list
//  }

  def deleteAll(implicit s: Session) {
    pages.delete
  }

  def listCovers(implicit s: Session) = {
    val query =
      (for {
        ((series, story),page) <- seriesQ rightJoin stories on (_.id === _.seriesId) rightJoin pages on (_._2.id === _.storyId)
        if (page.isCover)
      } yield (series, story, page))
      Logger.debug(s"SelectStatement:${query.selectStatement}")
      query.list
    }
  
  def listCoversBySeries(seriesId: Long)(implicit s: Session) = {
    val query =
      (for {
        ((series, story),page) <- seriesQ rightJoin stories on (_.id === _.seriesId) rightJoin pages on (_._2.id === _.storyId)
        if (story.seriesId === seriesId && page.isCover)
      } yield (series, story, page))
    
    query.list
  }


}
