package blog.media{

 //media
  class Picture(resourceId: String, path: String, format: String)
      extends Media(resourceId: String, path: String) {

    def getExtension: String = format
    def getWidth: Int = 1
    def getLength: Int = 12
    def resize(width: Int, height: Int): Unit = println("resizing")

  }

  class Video(
      resourceId: String,
      path: String,
      format: String,
      encoding: String
  ) extends Media(resourceId: String, path: String) {

    def getEncoding: String = encoding
    def loop(autoloop: Boolean): Boolean = true

  }

  object FileHandler {

    def uploadResource(resourceId: String): Boolean = true

    def editResource(resourceId: String): Boolean = true

    def removeResource(resourceId: String): Boolean = true

    def retrieveResource(path: String): String = path

  }

  class Media(val resourceId: String, val path: String) {

    def getFileName(resourceId: String): String =
      FileHandler.retrieveResource(path)

    def getSize(resourceId: String): Int =
      FileHandler.retrieveResource(path).length


  }

}