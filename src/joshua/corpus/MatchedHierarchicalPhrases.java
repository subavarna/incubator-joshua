/* This file is part of the Joshua Machine Translation System.
 * 
 * Joshua is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */
package joshua.corpus;

import joshua.corpus.suffix_array.PatternFormat;

/**
 * Represents a list of matched hierarchical phrases.
 * 
 * @author Lane Schwartz 
 * @since Apr 4 2009
 * @version $LastChangedDate$
 */
public interface MatchedHierarchicalPhrases extends PatternFormat {

	/**
	 * Gets the number of contiguous sequences of terminals
	 * in the pattern represented by this object.
	 * 
	 * @return The number of contiguous sequences of terminals
	 * in the pattern represented by this object.
	 */
	int getNumberOfTerminalSequences();
	
	/**
	 * 
	 * @param phraseIndex
	 * @param positionNumber
	 * @return
	 */
	int getStartPosition(int phraseIndex, int positionNumber);
	
	/**
	 * 
	 * @param phraseIndex
	 * @param positionNumber
	 * @return
	 */
	int getEndPosition(int phraseIndex, int positionNumber);
	
	
	/**
	 * Gets the number of locations in the corpus 
	 * that match the pattern.
	 * 
	 * @return The number of locations in the corpus 
	 * that match the pattern.
	 */
	int size();
	
	/**
	 * Tests if this list has no matches in the corpus.
	 * 
	 * @return <code>true</code> if this list has no matches in the corpus,
	 *         <code>false</code> otherwise.
	 */
	boolean isEmpty();
	
	/** 
	 * Gets the index of the sentence from which the specified phrase was extracted.
	 *  
	 * @param phraseIndex Index of a phrase
	 * @return the index of the sentence from which the specified phrase was extracted.
	 */
	int getSentenceNumber(int phraseIndex);
	
	/**
	 * Gets the number of terminal tokens in the specified terminal sequence.
	 * 
	 * @param i Index of a terminal sequence in this object's pattern.
	 * @return the number of terminal tokens in the specified terminal sequence
	 */
	int getTerminalSequenceLength(int i);
	
	/**
	 * Constructs a new object exactly the same as this object
	 * (specifically, it contains the exact same list of corpus matches),
	 * but prepends the nonterminal X to the pattern of the returned object.
	 * 
	 * @return list of matched phrases identical with updated pattern
	 */
	MatchedHierarchicalPhrases copyWithInitialX();
	
	/**
	 * Constructs a new object exactly the same as this object
	 * (specifically, it contains the exact same list of corpus matches),
	 * but appends the nonterminal X to the pattern.
	 * 
	 * @return list of matched phrases identical with updated pattern
	 */
	MatchedHierarchicalPhrases copyWithFinalX();
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// The methods below ideally would go into a HierarchicalPhrase class.
	//
	// However, for efficiency, they are instead incorporated into this class.
	///////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Gets the span in the backing corpus 
	 * of the phrase at the specified index.
	 * <p>
	 * <em>Note</em>: The span returned by this method
	 * is the span from the index of the first matched terminal in this phrase
	 * to the one past the index of the last matched terminal in this phrase.
	 * 
	 * @param Index of a matched phrase
	 * @return the span in the backing corpus 
	 *         of the phrase at the specified index
	 */
	Span getSpan(int phraseIndex);
	
	
	boolean containsTerminalAt(int phraseIndex, int alignmentPointIndex);
	int getFirstTerminalIndex(int phraseIndex);
	
	/**
	 * Gets the exclusive ending index of the last terminal sequence of the specified phrase.
	 * 
	 * @param phraseIndex
	 * @return
	 */
	int getLastTerminalIndex(int phraseIndex);
	int getTerminalSequenceStartIndex(int phraseIndex, int sequenceIndex);
	int getTerminalSequenceEndIndex(int phraseIndex, int sequenceIndex);
}