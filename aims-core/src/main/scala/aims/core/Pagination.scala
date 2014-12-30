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
case class Pagination[E](items: List[E], page: Int, total: Int, links: Seq[rel] = Seq(LinkParams.prev, LinkParams.next))

object Pagination {
  def empty[E] = Pagination[E](Nil, 0, 0, Nil)
}

case class Page(page: Int, perPage: Int)

object Page {
  def apply(query: Query): Page = {
    Page(query.getOrElse("page", "1").toInt, query.getOrElse("per_page", "100").toInt)
  }
}

