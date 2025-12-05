package tamanetwork

object TamaApiProvider {
    val tama_api: TamaApiService =
        TamaApiClient.tama_retrofit.create(TamaApiService::class.java)
}