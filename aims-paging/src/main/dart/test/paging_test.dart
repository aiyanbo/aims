import 'package:unittest/unittest.dart';
import 'package:aims-paging/paging.dart';

void main() {
  test("has last", () {
    expect(Pagination.parse('<http://10.200.182.54:8080/coupons?page=2>; rel="next", <http://10.200.182.54:8080/coupons?page=20>; rel=last').totalPage, equals(20));
  });

  test("has prev", () {
    expect(Pagination.parse("<http://10.200.182.54:8080/coupons?page=1>; rel=first, <http://10.200.182.54:8080/coupons?page=19>; rel=prev").totalPage, equals(20));
  });
}