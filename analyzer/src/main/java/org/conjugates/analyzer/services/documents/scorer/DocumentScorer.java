package org.conjugates.analyzer.services.documents.scorer;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

import org.conjugates.analyzer.services.documents.DocumentScore;
import org.conjugates.analyzer.services.documents.Entity;
import org.conjugates.analyzer.services.documents.EntityScore;
import org.conjugates.analyzer.services.documents.ParsedDocument;
import org.conjugates.analyzer.services.documents.ParsedSentence;
import org.conjugates.analyzer.services.documents.Sentiment;
import org.springframework.stereotype.Service;

@Service
public class DocumentScorer {

  public DocumentScore score(ParsedDocument parsedDocument) {
    Multimap<Entity, ParsedSentence> entityToReferences =
        createEntityToReferenceMap(parsedDocument);
    Collection<EntityScore> entityScores = calculateEntityScores(entityToReferences);
    int numSentences = parsedDocument.getSentences().size();
    int aggregateSentenceScore = aggregateSentenceScore(parsedDocument.getSentences());
    int averageScore = aggregateSentenceScore / numSentences;
    return new DocumentScore(entityScores, averageScore, numSentences);
  }

  private static Collection<EntityScore> calculateEntityScores(
      Multimap<Entity, ParsedSentence> entityToReferences) {
    return FluentIterable.from(entityToReferences.keySet()).transform((Entity entity) -> {
      Collection<ParsedSentence> references = entityToReferences.get(entity);
      return calculateEntityScore(entity, references);
    })
    .toList();
  }

  /**
   * 0 is very negative.
   * 100 is very positive.
   * 50 is neutral.
   */
  private static EntityScore calculateEntityScore(
      Entity entity,
      Collection<ParsedSentence> references) {
    int numberOfMentions = references.size();
    int aggregateScore = aggregateSentenceScore(references);
    int averageScore = aggregateScore / numberOfMentions;
    return new EntityScore(
        entity,
        averageScore,
        aggregateScore,
        numberOfMentions,
        numberOfMentions);
  }

  private static int aggregateSentenceScore(Collection<ParsedSentence> sentences) {
    int aggregateScore = 0;
    for (ParsedSentence sentence : sentences) {
      aggregateScore += scoreSentiment(sentence.getSentiment());
    }
    return aggregateScore;
  }

  private static int scoreSentiment(Sentiment sentiment) {
    if (sentiment == Sentiment.VERY_NEGATIVE) {
      return 0;
    } else if (sentiment == Sentiment.NEGATIVE) {
      return 25;
    } else if (sentiment == Sentiment.NEUTRAL) {
      return 50;
    } else if (sentiment == Sentiment.POSITIVE) {
      return 75;
    } else if (sentiment == Sentiment.VERY_POSITIVE) {
      return 100;
    } else {
      throw new IllegalArgumentException("Unknown sentiment: " + sentiment);
    }
  }

  private static Multimap<Entity, ParsedSentence> createEntityToReferenceMap(
      ParsedDocument parsedDocument) {
    Multimap<Entity, ParsedSentence> entityToReferences = HashMultimap.create();
    for (ParsedSentence sentence : parsedDocument.getSentences()) {
      for (Entity entity : sentence.getEntities()) {
        entityToReferences.put(entity, sentence);
      }
    }
    return entityToReferences;
  }

}
