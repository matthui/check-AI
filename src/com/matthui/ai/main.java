package com.matthui.ai;

import java.util.List;

import com.briansea.gamecabinet.game.Game;
import com.briansea.gamecabinet.game.Move;
import com.briansea.gamecabinet.game.Player;

public class main extends Player{

	private String myTurn;
	
	public main () {
		this.nameProperty().set("checkersAI");
	}
	public void makeMove( Game gs, List<Move> move ) {
		myTurn = gs.whoseTurn();
		// this function makes the root
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int maxDepth = 8;
		List<List<Move>> validMoves = gs.getValidMoves();
		for( List<Move> vm : validMoves ) {
			Game newG = gs.deepCopy();
			newG.makeMove(vm);
			MMNode c = new MMNode(newG, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
			// Make an MMNode appropriately and run alphabeta on it
			int rtn = c.alphabeta(c.depth);
					
			if( rtn > alpha ) {
				alpha = rtn;
				move.clear();
				// Copy the validMove into move
			}
		}
		
	}
	
	private class MMNode {
		private Game gs;
		private int alpha;
		private int beta;
		private boolean isMax;
		
		public MMNode( Game gs, int alpha, int beta, boolean isMax ) {
			this.gs = gs;
			this.alpha = alpha;
			this.beta = beta;
			this.isMax = isMax;
		}

		public int alphabeta(int depth ) {
			if( depth == 0 ) {
				return this.score();
			}
			List<List<Move>> validMoves = this.gs.getValidMoves();
			for( List<Move> vm : validMoves ) {
				if( this.alpha >= this.beta ) {
					//pruning
					break;
				}
				Game newG = gs.deepCopy();
				newG.makeMove(vm);
				MMNode c = new MMNode(gs, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
				int result = c.alphabeta(depth-1);
				if(c.isMax) {
					if(result > c.alpha) {
						c.alpha = result;
					}
				} else {
					if (result < c.beta) {
						c.beta = result;
					}
				}
				if(c.alpha >= c.beta || (System.currentTimeMillis() - startTime) > 3990) {
					c.isMax = true;
				}
				// Create a child MMNode appropriately and call alphabeta on it		
				// Use the return to change alpha or beta correctly
			}
			if (isMax) {
				return this.alpha;
			} else {
				return this.beta;
			}
		}
		
		public int score() {
			return this.gs.getScore(this.gs.getPlayer(myTurn));
		}
	}
}