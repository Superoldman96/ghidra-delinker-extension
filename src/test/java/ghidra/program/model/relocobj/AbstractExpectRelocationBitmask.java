/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.program.model.relocobj;

public abstract class AbstractExpectRelocationBitmask extends AbstractExpectRelocation {
	private final long address;
	private final int width;
	private final long bitmask;
	private final String symbolName;
	private final long addend;

	public AbstractExpectRelocationBitmask(long address, int width, String symbolName,
			long addend) {
		this(address, width, Relocation.getBitmask(width), symbolName, addend);
	}

	public AbstractExpectRelocationBitmask(long address, int width, long bitmask, String symbolName,
			long addend) {
		this.address = address;
		this.width = width;
		this.bitmask = bitmask;
		this.symbolName = symbolName;
		this.addend = addend;
	}

	// This equals() method is intentionally not implementing an equivalence relation.
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractRelocationBitmask)) {
			return false;
		}

		AbstractRelocationBitmask relocation = (AbstractRelocationBitmask) obj;
		return address == relocation.getAddress().getOffset() && width == relocation.getWidth() &&
			bitmask == relocation.getBitmask() && symbolName.equals(relocation.getSymbolName()) &&
			addend == relocation.getAddend();
	}
}
