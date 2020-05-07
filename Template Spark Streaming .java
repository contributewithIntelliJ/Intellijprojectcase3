 private var sparkSession: SparkSession = null
  private var sparkConf : SparkConf = _
  private var streamingContext : StreamingContext = _
  private var kafkaConsumer : InputDStream[ConsumerRecord[String, String]] = _

  private val logger = LogManager.getLogger("[ApplicationContext]")

  /**
    * Cette application retourne une session Spark
    */
  def getSparkSession(applicationName: String, isLocal: Boolean = false): SparkSession = {

    if (isLocal) {
      System.setProperty("hadoop.home.dir", "C:/Users/X176814/Downloads/bin")
      sparkSession = SparkSession.builder
        .master("local[*]")
        .enableHiveSupport()
        .config("spark.sql.crossJoin.enabled", "true")
        .getOrCreate()
    } else {
      sparkSession = SparkSession.builder
        .appName(applicationName)
        .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .config("spark.sql.crossJoin.enabled", "true")
        .config("spark.kryoserializer.buffer.max", "512m")
        .config("spark.scheduler.mode", "FAIR")
        .enableHiveSupport()
        .getOrCreate()
    }

    sparkSession
  }

  /**
    * cette fonction retourne un singleton du context Spark Streaming n�cessaire pour le calcul des indicateurs streaming du projet
    * @param sparkStreamingApplicationName : le nom de l'application
    * @param sparkStreamingBatchDuration : la dur�e du batch Streaming en Spark
    * @param isLocal : (mode Cluster) par d�faut, cette variable indique si on lance l'application en local ou en production
    * @return : le singleton de Spark Streaming
    */
  def getStreamingContext(sparkStreamingApplicationName : String, sparkStreamingBatchDuration : Int, isLocal : Boolean = false) : StreamingContext = {

    logger.info("Getting streamingContext Object...")

    try{
      if(sparkConf == null){
        sparkConf = new SparkConf()
          .setAppName(sparkStreamingApplicationName)
      }

      if (isLocal) {
        sparkConf.setMaster("local[*]")
      }

      if(streamingContext == null){
        logger.warn("The streamingContext Object is null, initializing a new one...")
        streamingContext = new StreamingContext(sparkConf, Seconds(sparkStreamingBatchDuration))
      }
    }catch {
      case ex : Exception => logger.error(s"An Exception occured when getting the streaming context :\n${ex.printStackTrace()}")
    }

    streamingContext
  }