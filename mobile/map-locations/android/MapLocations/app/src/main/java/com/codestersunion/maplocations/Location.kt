import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class LocationData(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    @Json(name = "attributes") val rawAttributes: List<Attribute>,
    @Transient var locationType: String = "",
    @Transient var name: String = "",
    @Transient var description: String = "",
    @Transient var estimatedRevenue: Double = 0.0
) {
    fun processAttributes() {
        for (attribute in rawAttributes) {
            when (attribute.type) {
                "location_type" -> locationType = attribute.value.toString()
                "name" -> name = attribute.value.toString()
                "description" -> description = attribute.value.toString()
                "estimated_revenue_millions" -> estimatedRevenue = (attribute.value as? Number)?.toDouble() ?: 0.0
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LocationData) return false
        return id == (other as? LocationData)?.id
    }
}

@JsonClass(generateAdapter = true)
data class Attribute(
    val type: String,
    val value: Any
)

class AttributeAdapter {
    @FromJson
    fun fromJson(json: Map<String, Any>): Attribute {
        return Attribute(
            type = json["type"] as String,
            value = json["value"] as Any
        )
    }

    @ToJson
    fun toJson(attribute: Attribute): Map<String, Any> {
        return mapOf(
            "type" to attribute.type,
            "value" to attribute.value
        )
    }
}

interface ApiService {
    @GET("https://raw.githubusercontent.com/codestersunion/coding-exercises/master/mobile/map-locations/locations.json")  // Replace with your actual endpoint
    suspend fun getLocations(): List<LocationData>
}

class LocationRepository(private val apiService: ApiService) {
    suspend fun fetchLocations(): List<LocationData> {
        return apiService.getLocations().map { location ->
            location.apply { processAttributes() }
        }
    }
}

// Set up Retrofit and Moshi
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(AttributeAdapter())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://your-api-base-url.com/")  // Replace with your actual base URL
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

val apiService = retrofit.create(ApiService::class.java)



