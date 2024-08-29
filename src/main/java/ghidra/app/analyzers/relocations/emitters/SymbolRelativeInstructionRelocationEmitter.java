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
package ghidra.app.analyzers.relocations.emitters;

import ghidra.app.analyzers.relocations.patterns.OperandMatch;
import ghidra.app.analyzers.relocations.utils.SymbolWithOffset;
import ghidra.app.util.importer.MessageLog;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.Instruction;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.MemoryAccessException;
import ghidra.program.model.relocobj.RelocationTable;
import ghidra.program.model.symbol.Reference;
import ghidra.program.model.symbol.Symbol;
import ghidra.util.task.TaskMonitor;

public abstract class SymbolRelativeInstructionRelocationEmitter
		extends InstructionRelocationEmitter {
	protected final Symbol fromSymbol;

	public SymbolRelativeInstructionRelocationEmitter(Program program,
			RelocationTable relocationTable, Function function, Symbol fromSymbol,
			TaskMonitor monitor, MessageLog log) {
		super(program, relocationTable, function, monitor, log);

		this.fromSymbol = fromSymbol;
	}

	@Override
	public boolean evaluate(Instruction instruction, OperandMatch match,
			SymbolWithOffset symbol, Reference reference)
			throws MemoryAccessException {
		long origin = fromSymbol.getAddress().getUnsignedOffset();
		long relative = match.getValue();
		long target = reference.getToAddress().getUnsignedOffset();

		return target == origin + relative;
	}

	@Override
	public void emit(Instruction instruction, OperandMatch match, SymbolWithOffset symbol,
			Reference reference) {
		RelocationTable relocationTable = getRelocationTable();
		Address address = instruction.getAddress().add(match.getOffset());
		long addend = reference.getToAddress().getUnsignedOffset() - symbol.address;

		relocationTable.addRelativeSymbol(address, match.getSize(), match.getBitmask(), symbol.name,
			addend, fromSymbol.getName());
	}
}
