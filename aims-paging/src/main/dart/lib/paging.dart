library paging;

class Pagination {

  bool hasFirst = false;
  bool hasPrev = false;
  bool hasNext = false;
  bool hasLast = false;

  String first;
  String prev;
  String next;
  String last;

  int totalPage;

  static Pagination parse(String linkHeader) {
    Pagination pagination = new Pagination();
    if (linkHeader.isNotEmpty) {
      var links = linkHeader.split(",");
      links.forEach((String link) {
        var settings = link.trim().split(";");
        String uri = settings.first.replaceAllMapped(new RegExp("<([^>]*)>"), (match) {
          return match.group(1);
        });
        settings.forEach((String setting) {
          if (setting.trim().startsWith(new RegExp(r'rel="?first"?'))) {
            pagination.hasFirst = true;
            pagination.first = uri;
          } else if (setting.trim().startsWith(new RegExp(r'rel="?prev"?'))) {
            pagination.hasPrev = true;
            pagination.prev = uri;
          } else if (setting.trim().startsWith(new RegExp(r'rel="?next"?'))) {
            pagination.hasNext = true;
            pagination.next = uri;
          } else if (setting.trim().startsWith(new RegExp(r'rel="?last"?'))) {
            pagination.hasLast = true;
            pagination.last = uri;
          }
        });
      });
      if (pagination.hasLast) {
        pagination.totalPage = int.parse(Uri.parse(pagination.last).queryParameters["page"]);
      } else if (pagination.hasPrev) {
        pagination.totalPage = int.parse(Uri.parse(pagination.prev).queryParameters["page"]) + 1;
      }
    }
    return pagination;
  }
}
