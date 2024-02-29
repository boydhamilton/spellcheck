# spellcheck

Quick spelling correction program using Levenshtein distance and n-grams in Java. Gives mispelled words spelling suggestions of an input phrase based off a [database of 370,101 English words](https://www.kaggle.com/datasets/ruchi798/part-of-speech-tagging/data) as our dictionary, and one of the [333,333 most common words](https://www.kaggle.com/datasets/rtatman/english-word-frequency?resource=download). Algorithm generates guesses of correct spellings based off of matrix distance, then sorts the guesses by popularity using unigrams. Because none of the data uses sentances or words in sequence, we cannot use any order of n-gram higher than one, which renders us unable to use context to provide suggestions.

[Source paper](https://www.irjet.net/archives/V8/i9/IRJET-V8I9316.pdf)
[Source paper](https://fileadmin.cs.lth.se/cs/education/EDA171/Reports/2009/david.pdf)

![Levenshtein Formula](github-resources/levenshtein-formula.png "Levenshtein Formula")
