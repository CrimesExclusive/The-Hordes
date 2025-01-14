package net.smileycorp.hordes.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.smileycorp.hordes.common.Constants;
import net.smileycorp.hordes.hordeevent.IOngoingHordeEvent;

public class HordeEndEvent extends HordeEvent {

	protected final BlockPos pos;
	protected String message = Constants.hordeEventEnd;
	protected final boolean wasCommand;

	public HordeEndEvent(EntityPlayer player, IOngoingHordeEvent horde, boolean wasCommand) {
		super(player, horde);
		pos = player.getPosition();
		this.wasCommand = wasCommand;
	}

	public BlockPos getPlayerPos() {
		return pos;
	}

	//Whether the event was ended due to a command
	public boolean wasCommand() {
		return wasCommand;
	}

	//get the translation key for the end message
	public String getMessage() {
		return message;
	}

	//set the translation key for the end message
	public void setMessage(String message) {
		this.message = message;
	}
}
