package aims.slick

import aims.core.{ Page, Pagination }
import aims.cqrs.OperationService
import aims.model.Event
import akka.http.model.Uri.Query

import scala.slick.jdbc.JdbcBackend.{ DatabaseDef, SessionDef }

/**
 * Component:
 * Description:
 * Date: 14/12/26
 * @author Andy Ai
 */
trait SlickOperationService[E] extends OperationService[E] {

  def queryDatabase(): DatabaseDef

  def commandDatabase(): DatabaseDef

  val queryDef = queryDatabase()

  val commandDef = commandDatabase()

  override def get(event: Event): Option[E] = {
    queryDef withSession {
      implicit session ⇒
        getWithSession(event.extractions.asInstanceOf[Product], event.request.uri.query)
    }
  }

  override def pagination(event: Event): Pagination[E] = {
    queryDef withSession {
      implicit session ⇒
        val query = event.request.uri.query
        event.extractions match {
          case extractions: Product ⇒ paginationWithSession(extractions, Page(query), query)
          case _                    ⇒ paginationWithSession(null, Page(query), query)
        }
    }
  }

  override def modify(event: Event): Unit = {
    commandDef withSession {
      implicit session ⇒
        modifyWithSession(event.extractions.asInstanceOf[Product], event.payload.get)
    }
  }

  override def update(event: Event): Unit = {
    commandDef withSession {
      implicit session ⇒
        updateWithSession(event.extractions.asInstanceOf[Product], event.payload.get)
    }
  }

  override def insert(event: Event): Any = {
    commandDef withSession {
      implicit session ⇒
        event.extractions match {
          case extractions: Product ⇒ insertWithSession(event.extractions.asInstanceOf[Product], event.payload.get)
          case _                    ⇒ insertWithSession(null, event.payload.get)
        }
    }
  }

  override def delete(event: Event): Unit = {
    commandDef withSession {
      implicit session ⇒
        deleteWithSession(event.extractions.asInstanceOf[Product])
    }
  }

  def updateWithSession(extractions: Product, entity: String)(implicit session: SessionDef): Unit

  def modifyWithSession(extractions: Product, entity: String)(implicit session: SessionDef): Unit

  def insertWithSession(extractions: Product, entity: String)(implicit session: SessionDef): Option[E]

  def getWithSession(extractions: Product, query: Query)(implicit session: SessionDef): Option[E]

  def deleteWithSession(extractions: Product)(implicit session: SessionDef): Unit

  def paginationWithSession(extractions: Product, page: Page, query: Query)(implicit session: SessionDef): Pagination[E]

}
