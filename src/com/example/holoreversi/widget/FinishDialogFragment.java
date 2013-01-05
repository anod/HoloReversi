package com.example.holoreversi.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.holoreversi.R;
import com.example.holoreversi.model.board.Board;

public class FinishDialogFragment extends DialogFragment {

	public static FinishDialogFragment newInstance(int scoreBlack, int scoreWhite, int winner) {
		FinishDialogFragment frag = new FinishDialogFragment();
        Bundle args = new Bundle();
        args.putInt("scoreBlack", scoreBlack);
        args.putInt("scoreWhite", scoreWhite);
        args.putInt("winner", winner);
        frag.setArguments(args);
        return frag;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int scoreBlack = getArguments().getInt("title");
		int scoreWhite = getArguments().getInt("scoreWhite");
		int winner = getArguments().getInt("winner");
		
		return createFinishDialog(scoreBlack, scoreWhite, winner, getActivity(), null);
	}
	
	public static Dialog createFinishDialog(int scoreBlack, int scoreWhite, int winner, Context context, OnCancelListener cancelListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String playerStr;
		if (winner == Board.WHITE) {
			playerStr = context.getString(R.string.player_name_white) + context.getString(R.string.player_won);
		} else if(winner == Board.BLACK) {
			playerStr = context.getString(R.string.player_name_blue) + context.getString(R.string.player_won);
		} else {
			playerStr = context.getString(R.string.tie);
		}
		playerStr += "\n" + context.getString(R.string.scoreBlack, scoreBlack);
		playerStr += "\n" + context.getString(R.string.scoreWhite, scoreWhite);
		builder
			.setMessage(playerStr)
			.setCancelable(true);
		if (cancelListener != null) {
			builder.setOnCancelListener(cancelListener);
		}
		
		return builder.create();
	}
}
