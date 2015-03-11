package aims.core

import akka.http.model.Uri.Query
import akka.http.model.headers.LinkParams
import akka.http.model.headers.LinkParams.rel

/**
 * Component:
 * Description:
 * Date: 2014/12/30
 * @author Andy Ai
 */
case class Pagination[E](items: List[E], page: Int, totalPage: Int, totalCount: Int, links: Seq[rel] = Pagination.pagerAlternative)

object Pagination {
  lazy val multiPage: Seq[rel] = Seq(LinkParams.first, LinkParams.prev, LinkParams.next, LinkParams.last)
  lazy val pagerAlternative = Seq(LinkParams.prev, LinkParams.next)

  def empty[E]: Pagination[E] = Pagination[E](Nil, 0, 0, 0, Nil)

  def totalPage(totalCount: Int, perPage: Int): Int = {
    (totalCount + perPage - 1) / perPage
  }
}

case class Page(page: Int, perPage: Int)

object Page {
  def apply(query: Query): Page = {
    Page(query.getOrElse("page", "1").toInt, query.getOrElse("per_page", "100").toInt)
  }
}
