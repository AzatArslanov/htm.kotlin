# Implementation of Hierarchical Temporal Memory
**This project was created to better understand the theory of HTM**

Hierarchical temporal memory (HTM) is a biologically constrained theory of machine intelligence originally described in 
the 2004 book On Intelligence by Jeff Hawkins with Sandra Blakeslee. HTM is based on neuroscience and the physiology 
and interaction of pyramidal neurons in the neocortex of the human brain.[[Wiki]](https://en.wikipedia.org/wiki/Hierarchical_temporal_memory) 

See more at [hierarchical-temporal-memory](https://numenta.org/hierarchical-temporal-memory/)

# HTM Region

```kotlin
/* HTM region is logically comprised of a set of columns */
val regionSize = 10 // Count of columns in Region
val cellsPerColumn = 4 // Each column is comprised of one or more cells. 
val inputSize = 5 // Size of subset of the input bits

/* Initializiton */

val region = Region(regionSize, inputSize, cellsPerColumn) {

            spatialPooling {
                minOverlap = 5 /* A minimum number of inputs that must be active for a column to be considered 
                during the inhibition step */
                potentialPoolSize =  0.4 // Ratio of inputs selected from the input space during the initialization.
                connectedPermThreshold = 0.5 /* If the permanence value for a synapse is greater than this value, 
                it is said to be connected. */
                connectedPermInitialRange = 0.2 // Small range around connectedPermThreshold.
                inhibitionRadius = 0 // Average connected receptive field size of the columns.
                desiredLocalActivity = 0.3 /* A parameter controlling the number of columns 
                that will be winners after the inhibition step. */
                permanenceInc = 0.1 // Amount permanence values of synapses are incremented during learning.
                permanenceDec = 0.1 // Amount permanence values of synapses are decremented during learning.        
            }
        }

```

