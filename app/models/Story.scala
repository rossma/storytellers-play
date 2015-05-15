package models

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import scala.slick.lifted.Tag

case class Story(id: Option[Long], title: String, summary: String, seriesId: Option[Long] = None, created: DateTime)

class StoryTbl(tag: Tag) extends Table[Story](tag, "STORY") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def title = column[String]("TITLE", O.NotNull)
  def summary = column[String]("SUMMARY")
  def seriesId = column[Long]("SERIES_ID", O.NotNull)
  def created = column[DateTime]("CREATION_DATE", O.NotNull)
  def * = (id.?, title, summary, seriesId.?, created) <> (Story.tupled, Story.unapply _)
}

object StoryDAO {
  val stories = TableQuery[StoryTbl]

  def findById(id: Long)(implicit s: Session): Option[Story] =
    stories.filter(_.id === id).firstOption

  def count(implicit s: Session): Int =
    Query(stories.length).first

  def insert(story: Story)(implicit s: Session) {
    stories.insert(story)
  }
  
  /**
   * Insert a new story by ignoring auto inc column
   */
  def forceInsert(story: Story)(implicit s: Session) {
    stories.forceInsert(story)
  }

  def list(implicit s: Session) = {
    stories.list
  }

  def deleteAll(implicit s: Session) {
    stories.delete
  }

}
