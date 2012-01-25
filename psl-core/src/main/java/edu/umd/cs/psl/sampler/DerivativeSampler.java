/*
 * This file is part of the PSL software.
 * Copyright 2011 University of Maryland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.umd.cs.psl.sampler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.umd.cs.psl.evaluation.process.RunningProcess;
import edu.umd.cs.psl.model.kernel.GroundKernel;
import edu.umd.cs.psl.model.kernel.Kernel;
import edu.umd.cs.psl.reasoner.function.AtomFunctionVariable;

public class DerivativeSampler extends LinearSampler {

	private transient Map<Kernel, Double> totals;
	
	public DerivativeSampler(RunningProcess p, Collection<Kernel> k) {
		this(p, k, defaultMaxNoSteps,defaultSignificantDigits);
	}
	
	public DerivativeSampler(RunningProcess p, Collection<Kernel> k, int maxNoSteps) {
		this(p, k, maxNoSteps,defaultSignificantDigits);
	}
 	
	public DerivativeSampler(RunningProcess p, Collection<Kernel> k, int maxNoSteps, int significantDigits) {
		super(p, maxNoSteps, significantDigits);
		totals = new HashMap<Kernel, Double>();
		for (Kernel kernel : k) {
			totals.put(kernel, 0.0);
		}
	}
	
	public double getAverage(Kernel k) {
		return totals.get(k) / getNoSamples();
	}

	@Override
	protected void processNewDimension(AtomFunctionVariable var, int index) {
		/* Intentionally blank */
	}

	@Override
	protected void processSampledPoint(Iterable<GroundKernel> groundKernels) {
		for (GroundKernel gk : groundKernels) {
    		Kernel k = gk.getKernel();
    		if (totals.containsKey(k)) {
    			totals.put(k, totals.get(k) + gk.getIncompatibility());
    		}
    	}
	}
	
}