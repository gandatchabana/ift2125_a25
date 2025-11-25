package ift2015.tp3;

import java.util.*;


/*
* ACADEMIC INTEGRITY ATTESTATION
*
* [ x ] I certify that I have not used any generative AI tool
*
to solve this problem .
*
* [ ] I have used one or more generative AI tools .
*
Details below :
*
*
Tool ( s ) used : _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
*
*
Reason ( s ) for use :
*
___________________________________________________
*
___________________________________________________
*
*
Affected code sections :
*
___________________________________________________
*
___________________________________________________
*/



public class Q3 {
    
    private static final int MOD = 1_000_000_007;
    

    private class Sample {
        long population;
        int index;
        int multiplier;
        
        public Sample(long population, int index, int multiplier) {
           this.population = population;
	   this.index = index;
	   this.multiplier = multiplier;
        }
    }
    
    private PriorityQueue<Sample> minHeap;
    private long[] currentPopulations;
    private int[] multipliers;
    
    public Q3(long[] currentPopulations, int[] multipliers) {
	assert currentPopulations.length == multipliers.length;
	this.currentPopulations = currentPopulations;
	this.multipliers = multipliers;
	this.minHeap = null;

    }
   
    public Q3() {
	this.currentPopulations = null;
	this.multipliers = null;
	this.minHeap = null;
    }
 

    private class PopulationSampleComparator implements Comparator<Sample> {
	@Override
	public int compare(Sample s1, Sample s2) {
		if (s1.population > s2.population) {return 1;}
		else if (s1.population < s2.population) {return -1;}
		else {return 0;}
	}
    }


    private class IndexSampleComparator implements Comparator<Sample> {
	@Override
	public int compare(Sample s1, Sample s2) {
		if (s1.index > s2.index) {return 1;}
		else if (s1.index < s2.index) {return -1;}
		else {return 0;}
	}
    }


    private boolean IndexSampleComparatorTest() {
	IndexSampleComparator indexSampleComparator = new IndexSampleComparator();
	long[] populations = {1, 2, 3};
	int[] multipliers  = {2, 2, 2};
	PriorityQueue<Sample> pq = _buildHeap(populations, multipliers);
	Sample s1 = pq.poll();
	Sample s2 = pq.poll();
	int result = indexSampleComparator.compare(s1, s2);
	assert (result != 0 && s1.index != s2.index) : "Index 1: " + s1.index + " and Index 2: " + s2.index;
	System.out.print("Index 1: " + s1.index);
	System.out.print("\tIndex 2: " + s2.index);
	System.out.println("\tresult: " + result);
	return true;
    }

    private PriorityQueue<Sample> _buildHeap(long[] populations, int[] multipliers) {
        // TODO: 
	PriorityQueue<Sample> minHeap = new PriorityQueue<Sample>(new PopulationSampleComparator());
	// Create Sample for each
	for (int idx=0; idx<populations.length; idx++) {
		Sample sample = new Sample(populations[idx], idx, multipliers[idx]);
		// Insert into PriorityQueue
		minHeap.add(sample);
	}
	return minHeap;
    }


    public void buildHeap(long[] populations, int[] multipliers) {
        // TODO: 
	if (this.minHeap == null) { this.minHeap = new PriorityQueue<Sample>( new PopulationSampleComparator()); }
	// Create Sample for each
	for (int idx=0; idx<populations.length; idx++) {
		Sample sample = new Sample(currentPopulations[idx], idx, multipliers[idx]);
		// Insert into PriorityQueue
		minHeap.add(sample);
	}

	this.currentPopulations = populations;
	this.multipliers = multipliers;
    }
    

    public long[] simulate(int cycles) {
        // TODO:
	if (minHeap == null) {buildHeap(this.currentPopulations, this.multipliers);}
	// At each cycle, extract the min, multiply it by its multiplier, reinsert it
	for (int cycle=1; cycle<=cycles; cycle++) {
		// Extract the min from the min heap
		Sample min_sample = minHeap.poll();
		// Compute the new population
		min_sample.population *= min_sample.multiplier;
		// Add the newly computed population to the min heap
		minHeap.add(min_sample);
		// Inplace substitution of currentPopulations with the new population computed
		// Can use .index as samples were created using the index of initial population array `population` iterator
		//this.currentPopulations[min_sample.index] = min_sample.population;
		
		// Print pop at end of cycle
		String cur_pop = "";
		Iterator<Sample> iter = minHeap.iterator();
		while (iter.hasNext()) {
			Sample sample = iter.next();
			cur_pop += Long.toString(sample.population) + "(idx:" + Integer.toString(sample.index) + ")";
			cur_pop += " ";
		}
		System.out.println("At cycle:" + cycle + ", populations: " + cur_pop );
	}
	// Return finalPop[] after applying MOD to it
	long[] finalPopulations = new long[currentPopulations.length];
	// Returns array of Objects of current state of minHeap
	Object[] finalSamples_obj = minHeap.toArray();
	for (int idx=0; idx<finalSamples_obj.length; idx++) { 
		// For each sample (with explicit cast Object -> Sample
		Sample finalSample = (Sample)finalSamples_obj[idx];
		// Add sample population modulo MOD to finalPop[]
		finalPopulations[idx] = finalSample.population % MOD;
	}
	//Sample[] finalSamples = new Sample[finalSamples_obj.length];
	//for (int idx=0; idx<finalSamples_obj.length; idx++) { finalSamples[idx] = (Sample)finalSamples_obj[idx]; finalPopulations[idx] = finalSamples_obj[idx].population % MOD; }
	//for (int idx=0; idx<finalSamples.length; idx++) { finalPopulations[idx] = finalSamples[idx].population % MOD; }
	
        return finalPopulations;
    }
 

    public int[] simulateOptimized(int cycles) {
    	return null;
    }
    

    private String displayArrayInt(int[] arr) {
	System.out.print("[");
	for (int elem : arr) {
		System.out.print(elem + ", ");
	}
	System.out.print("]");
	return "";
    }


    private String displayArrayLong(long[] arr) {
	System.out.print("[");
	for (long elem : arr) {
		System.out.print(elem + ", ");
	}
	System.out.print("]");
	return "";
    }


    public long[] getCurrentState() {
        // TODO:
	if (this.minHeap == null) {
		System.out.println("minHeap is null, building one with populations " + displayArrayLong(this.currentPopulations) + " and multipliers " + displayArrayInt(this.multipliers));
		buildHeap(this.currentPopulations, this.multipliers);
	}
	Iterator<Sample> iter = this.minHeap.iterator();
	// long[] to store populations
	var currentPopulations = new long[this.currentPopulations.length];
	// HashMap<Integer, Long> to store (Sample.index: Sample.population)
	//HashMap<Integer, Long> popAtCurrent = new HashMap<Integer, Long>();
	//var idx = 0;

	// Get subsequent array of minHeap
	Object[] arr = minHeap.toArray();
	// Convert arr of Objects to arr of Sample
	Sample[] currentStateSamples = new Sample[arr.length];
	for (int idx=0; idx<arr.length; idx++) {
		Sample currentStateSample = (Sample)arr[idx]; 
		currentStateSamples[idx] = currentStateSample;
		// DEBUG: Print currentStateSamples index and pop
		System.out.print("DEBUG: ");
		System.out.println("At idx " + idx + ": " + "(" + currentStateSamples[idx].population + ", " + currentStateSamples[idx].index + ")");
	}
	
	// Sort using IndexSampleComparator
	Arrays.sort(currentStateSamples, new IndexSampleComparator());
	// Sort using Comparator.comparingInt
	//Arrays.sort(currentStateSamples, Comparator.comparingInt(Sample -> Sample.index));
	
	//DEBUG: print sorted array currentStateSamples
	System.out.print("DEBUG: currentStateSamples : [");
	for (Sample elem : currentStateSamples) {
		System.out.print("(" + elem.population + ", " + elem.index + ") ");
		
	}
	System.out.println("]");

	// Append member `population` from sorted array currentStateSamples to array currentPopulations
	for (var idx=0; idx<currentPopulations.length; idx++) {
		Sample sample = currentStateSamples[idx];
		currentPopulations[idx] = sample.population;
	}
	
	// Return currentPopulations
	return currentPopulations;
	//while (iter.hasNext()) {
	//	Sample sample = iter.next();
	//	currentPopulations[idx] = sample.population;
	//	idx++;
	//}
	//// Sort using comparator on .index
	//Arrays.sort(currentPopulations, new IndexSampleComparator());
        //return currentPopulations;
    }
    

    public int findMinIndex() {
        // TODO:
	if (this.minHeap == null) {buildHeap(this.currentPopulations, this.multipliers);}
	// Get subsequent array of minHeap
	Object[] arr = minHeap.toArray();
	// Convert arr of Objects to arr of Sample
	Sample[] currentStateSamples = new Sample[arr.length];
	for (int idx=0; idx<arr.length; idx++) {
		Sample currentStateSample = (Sample)arr[idx]; 
		currentStateSamples[idx] = currentStateSample;
	}
		
        return currentStateSamples[0].index;
    }
    

    public long predictPopulation(int sampleIndex, int futureCycles) {
        // TODO: 
        return 0;
    }


   public static void main(String[] args) {
	if (args.length > 0) {
		System.out.println("TEST");
		assert (args[0] == "test") : "Unrecognized CLI argument";
		Q3 q3 = new Q3();
		q3.IndexSampleComparatorTest();
		return;
	}

	long[] populations = {3, 5, 8};
	int[] multipliers = {3, 2, 4};

	Q3 q3 = new Q3(populations, multipliers);
	System.out.println("\n\tsimulate(5)");
	long[] finalPop = q3.simulate(5);
	System.out.println("final populations: " + Arrays.toString(finalPop));
	
	System.out.println("\n\tgetCurrentState");
	System.out.println(Arrays.toString(q3.getCurrentState()));

	System.out.println("\n\tfindMinIndex()");
	System.out.println("population index to be multiplied next cycle: " + q3.findMinIndex());
	return;
   }
    
}
