package dsragcode;

import java.util.stream.Stream;

import com.dtsx.astra.sdk.AstraDB;
import com.dtsx.astra.sdk.AstraDBCollection;

import io.stargate.sdk.data.domain.CollectionDefinition;
import io.stargate.sdk.data.domain.JsonDocument;
import io.stargate.sdk.data.domain.JsonDocumentResult;
import io.stargate.sdk.data.domain.SimilarityMetric;

public class DSRAGCode {

	static final String ASTRA_TOKEN = System.getenv("ASTRA_DB_APP_TOKEN");
	static final String ASTRA_API_ENDPOINT = System.getenv("ASTRA_API_ENDPOINT");

	public static void main(String[] args) {
	
		// conenct to DB
		AstraDB db = new AstraDB(ASTRA_TOKEN, ASTRA_API_ENDPOINT);
		
		// create collection
		CollectionDefinition colDefinition = CollectionDefinition.builder()
			    .name("vector_test")
			    .vector(5, SimilarityMetric.cosine)
			    .build();
		db.createCollection(colDefinition);
		AstraDBCollection collection = db.collection("vector_test");

		// Insert rows defined by key/value
		collection.insertOne(
		    new JsonDocument()
		        .id("1")
		        .put("text", "ChatGPT integrated sneakers that talk to you")
		        .vector(new float[]{0.1f, 0.15f, 0.3f, 0.12f, 0.05f}));
		
		// Perform a similarity search
		Stream<JsonDocumentResult> resultsSet = collection.findVector(
		    new float[]{0.15f, 0.1f, 0.1f, 0.35f, 0.55f}, 10
		);
		
		// print results
		for (JsonDocumentResult doc : resultsSet.toList()) {
			System.out.println(doc);
		}
	}
}
