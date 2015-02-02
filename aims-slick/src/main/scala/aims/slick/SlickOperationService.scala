package aims.slick

import aims.core.{ Page, Pagination }
import akka.http.model.Uri.Query

import scala.slick.jdbc.JdbcBackend.{ DatabaseDef, SessionDef }

/**
 * Component:
 * Description:
 * Date: 14/12/26
 * @author Andy Ai
 */
trait SlickOperationService[E] {

  def database(): DatabaseDef

  val databaseDef = database()

  def update(pathParameters: Map[String, String], entity: E): Unit = {
    databaseDef withSession {
      implicit session ⇒
        updateWithSession(pathParameters, entity)
    }
  }

  def insert(pathParameters: Map[String, String], entity: E): Option[E] = {
    databaseDef withSession {
      implicit session ⇒
        insertWithSession(pathParameters, entity)
    }
  }

  def get(pathParameters: Map[String, String], query: Query): Option[E] = {
    databaseDef withSession {
      implicit session ⇒
        getWithSession(pathParameters, query)
    }
  }

  def delete(pathParameters: Map[String, String]): Unit = {
    databaseDef withSession {
      implicit session ⇒
        deleteWithSession(pathParameters)
    }
  }

  def pagination(pathParameters: Map[String, String], page: Page, query: Query): Pagination[E] = {
    databaseDef withSession {
      implicit session ⇒
        paginationWithSession(pathParameters, page, query)
    }
  }

  def updateWithSession(pathParameters: Map[String, String], entity: E)(implicit session: SessionDef): Unit

  def insertWithSession(pathParameters: Map[String, String], entity: E)(implicit session: SessionDef): Option[E]

  def getWithSession(pathParameters: Map[String, String], query: Query)(implicit session: SessionDef): Option[E]

  def deleteWithSession(pathParameters: Map[String, String])(implicit session: SessionDef): Unit

  def paginationWithSession(pathParameters: Map[String, String], page: Page, query: Query)(implicit session: SessionDef): Pagination[E]

}
