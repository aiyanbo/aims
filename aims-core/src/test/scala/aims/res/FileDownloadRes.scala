package aims.res

import java.io.{File, FileOutputStream}
import java.nio.file.Files

import aims.http.AttachmentsDownloadRestlet
import aims.model.Event
import aims.routing.PatternMatcher
import com.google.common.base.Charsets

/**
 * Component:
 * Description:
 * Date: 15/3/10
 * @author Andy Ai
 */
class FileDownloadRes extends AttachmentsDownloadRestlet {

  override def attachmentsPattern(): PatternMatcher = "files"

  override def fromFile(event: Event): File = {
    val file = Files.createTempFile("我的中国心", ".txt").toFile
    val outputStream = new FileOutputStream(file)
    outputStream.write("download file test".getBytes(Charsets.UTF_8))
    file
  }

}
