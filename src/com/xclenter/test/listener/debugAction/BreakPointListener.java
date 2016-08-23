package com.xclenter.test.listener.debugAction;


import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.debug.core.model.IBreakpoint;
//import org.eclipse.cdt.debug.internal.core.breakpoints.AbstractLineBreakpoint;

public class BreakPointListener implements IBreakpointListener{

	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		try {
//			if(breakpoint instanceof AbstractLineBreakpoint){
//			  System.out.println(((AbstractLineBreakpoint)breakpoint).getAddress());
//			  System.out.println(((AbstractLineBreakpoint)breakpoint).getLineNumber());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		// TODO Auto-generated method stub
		System.out.println("breakPoint removed " + breakpoint.toString());
	}

	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		// TODO Auto-generated method stub
		System.out.println("breakPoint changed " + breakpoint.toString());
	}


}
