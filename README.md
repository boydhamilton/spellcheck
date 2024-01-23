# spellcheck

Quick spelling correction program using Levenshtein distance in Java. Gives mispelled words spelling suggestions of an input phrase based off a [database of 370,101 English words](https://www.kaggle.com/datasets/ruchi798/part-of-speech-tagging/data). Example contains recursive and matrix implementation with respective time complexities of `O(3^(m+n))` and `O(mn)`. Program uses matrix implementation, but recursive is present as a learning excercise.

[Source paper](https://www.irjet.net/archives/V8/i9/IRJET-V8I9316.pdf)

![Levenshtein Formula](github-resources/levenshtein-formula.png "Levenshtein Formula")
