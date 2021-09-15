package hu.zsoltkiss.lastfmsearch.model

import android.net.Uri
import android.util.Log
import com.google.gson.annotations.SerializedName


const val TAG_SEARCH = "__Search"
const val TAG_DETAILS = "__Details"
const val TAG_MAIN = "__Main"
const val TAG_IMAGE_URI = "__Uri"

data class ArtistAttribs(
    val name: String,
    val mbid: String,
    val url: String,
    val listeners: String,
    val image: List<ImageData>) {

    fun imageUriForSize(expectedSize: String): Uri? {
        return image.firstOrNull { it.size == expectedSize }?.let { imageData ->
            val url = imageData.text
            Log.d(TAG_IMAGE_URI, "$expectedSize: $url")
            Uri.parse(url)
        }
    }
}

data class ImageData(
    @SerializedName("#text")val text: String,
    val size: String
)

data class Wrapper(@SerializedName("artist") val items: List<ArtistAttribs>)
data class ArtistMatches(@SerializedName("artistmatches") val wrapper: Wrapper)
data class SearchResults(@SerializedName("results") val matches: ArtistMatches)


/*
*
*
* <results for="cher" xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">
  <opensearch:Query role="request" searchTerms="cher" startPage="1"/>
  <opensearch:totalResults>386</opensearch:totalResults>
  <opensearch:startIndex>0</opensearch:startIndex>
  <opensearch:itemsPerPage>20</opensearch:itemsPerPage>
  <artistmatches>
    <artist>
      <name>Cher</name>
      <mbid>bfcc6d75-a6a5-4bc6-8282-47aec8531818</mbid>
      <url>www.last.fm/music/Cher</url>
      <image_small>http://userserve-ak.last.fm/serve/50/342437.jpg</image_small>
      <image>http://userserve-ak.last.fm/serve/160/342437.jpg</image>
      <streamable>1</streamable>
    </artist>
	...
  </artistmatches>
</results>
*
*
*
* */

data class DetailsWrapper(@SerializedName("artist") val details: ArtistDetails)

data class ArtistDetails(
    val name: String,
    val mbid: String,
    val url: String,
    val image: List<ImageData>,
    val similar: Wrapper?
) {
    fun imageUriForSize(expectedSize: String): Uri? {
        return image.firstOrNull { it.size == expectedSize }?.let { imageData ->
            val url = imageData.text
            Log.d(TAG_IMAGE_URI, "$expectedSize: $url")
            Uri.parse(url)
        }
    }
}

/*
*
* <artist>
  <name>Cher</name>
  <mbid>bfcc6d75-a6a5-4bc6-8282-47aec8531818</mbid>
  <url>http://www.last.fm/music/Cher</url>
  <image size="small">http://userserve-ak.last.fm/serve/50/285717.jpg</image>
  <image size="medium">http://userserve-ak.last.fm/serve/85/285717.jpg</image>
  <image size="large">http://userserve-ak.last.fm/serve/160/285717.jpg</image>
  <streamable>1</streamable>
  <stats>
    <listeners>196440</listeners>
    <plays>1599101</plays>
  </stats>
  <similar>
    <artist>
      <name>Madonna</name>
      <url>http://www.last.fm/music/Madonna</url>
      <image size="small">http://userserve-ak.last.fm/serve/50/5112299.jpg</image>
      <image size="medium">http://userserve-ak.last.fm/serve/85/5112299.jpg></image>
      <image size="large">http://userserve-ak.last.fm/serve/160/5112299.jpg</image>
    </artist>
    ...
  </similar>
  <tags>
    <tag>
      <name>pop</name>
      <url>http://www.last.fm/tag/pop</url>
    </tag>
    ...
  </tags>
  <bio>
    <published>Thu, 13 Mar 2008 03:59:18 +0000</published>
    <summary>...</summary>
    <content>...</content>
  </bio>
</artist>
*
*
*
* */