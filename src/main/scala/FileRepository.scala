import com.mongodb.casbah.commons.Imports
import com.mongodb.casbah.gridfs.GridFS
import java.io.{ FileInputStream, InputStream, File }
import com.mongodb.casbah.gridfs.JodaGridFS
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.mongodb.casbah.gridfs.Implicits._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.gridfs.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.MongoDB
import org.bson.types.ObjectId
import com.mongodb.casbah.MongoClient
import java.io.ByteArrayInputStream

object Test extends App{
  RegisterJodaTimeConversionHelpers()

  val db = MongoClient()("db")
  val fr = new FileRepository(db)
  val id = fr.store("fn.txt", new ByteArrayInputStream("text".getBytes()), Some("text/plain")).get
  val file = fr.getFileFromMongo(id)
}

class FileRepository (personalDataDatabaseManager: MongoDB) {
  
  private val gridFS = JodaGridFS(personalDataDatabaseManager)

  def uploadFile(filename: String, friendlyName: String, contentType: Option[String] = None) =
    store(friendlyName, new FileInputStream(new File(filename)))

  def store(filename: String, stream: InputStream, contentType: Option[String] = None) = {
    val gridFile = gridFS.createFile(stream, filename)

    gridFile.filename = filename
    contentType.map(gridFile.contentType = _)
    gridFile.save
    gridFile._id
  }

  def delete(fileId: Imports.ObjectId) {
    gridFS.remove(fileId)
  }

  def getFileFromMongo(fileId: ObjectId): Option[(String, InputStream)] = {
    gridFS.findOne(fileId) match {
      case Some(file) => Some((file.filename.getOrElse("default_filename"), file.inputStream))
      case _ => None
    }
  }
}
