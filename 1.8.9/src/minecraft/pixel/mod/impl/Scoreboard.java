package pixel.mod.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import pixel.gui.Gui;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class Scoreboard extends ModDraggable {
	public Scoreboard() {
		super(false, 0, 0);
		
		loadOptions(
				new ModOption("show", true, new InGuiSettings("Show")),
				new ModOption(new ModOptionParent("show"), "showNumbers", true, new InGuiSettings("Show Numbers")),
				new ModOptionColor(new ModOptionParent("showNumbers"), "numbersColor", ColorManager.DEFAULT_RED.getARGB(), false, new ModOptionColor.InGuiSettings("Numbers Color", false, true)),
				new ModOption(new ModOptionParent("show"),"textShadow", false,  new InGuiSettings("Text Shadow")),
				new ModOptionColor(new ModOptionParent("show"), "backgroundColor", ColorManager.BLACK_50.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOptionColor(new ModOptionParent("show"), "headerBackgroundColor", ColorManager.BLACK_60.getARGB(), false, new ModOptionColor.InGuiSettings("Header Background Color", true, false))
				);
	}
	
	@Override
	public int getWidth() {
		if (!castOptionValueIntoBoolean("show")) return 0;
		
		ScoreObjective scoreObj = getDummyScoreObjective();
		net.minecraft.scoreboard.Scoreboard scoreboard = scoreObj.getScoreboard();
		Collection<Score> scores = scoreboard.getSortedScores(scoreObj);
		
		int width = font.getStringWidth(scoreObj.getDisplayName());
		
		for (Score score : scores) {
			ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
			String lineText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName()) + (castOptionValueIntoBoolean("showNumbers") ? ": " + score.getScorePoints() : "");
			
			width = Math.max(width, font.getStringWidth(lineText));
		}
		
		return width;
	}
	
	@Override
	public int getHeight() {
		if (!castOptionValueIntoBoolean("show")) return 0;
		
		ScoreObjective scoreObj = getDummyScoreObjective();
		int size = scoreObj.getScoreboard().getSortedScores(scoreObj).size();
		
		return font.FONT_HEIGHT + size * font.FONT_HEIGHT;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		if (castOptionValueIntoBoolean("show") && mc.theWorld.getScoreboard() != null) {
			net.minecraft.scoreboard.Scoreboard scoreboard = mc.theWorld.getScoreboard();
			ScoreObjective scoreObjective = null;
			ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(mc.thePlayer.getName());
			
			if (scorePlayerTeam != null) {
				int colorIndex = scorePlayerTeam.getChatFormat().getColorIndex();
				
				if (colorIndex >= 0) {
					scoreObjective = scoreboard.getObjectiveInDisplaySlot(3 + colorIndex);
				}
			}
			
			if (scoreObjective == null) {
				scoreObjective = scoreboard.getObjectiveInDisplaySlot(1);
			}
			
			if (scoreObjective != null) {
				renderScoreboard(pos, scoreObjective);
			}
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		if (castOptionValueIntoBoolean("show")) {
			ScoreObjective scoreObj = getDummyScoreObjective();
			
			renderScoreboard(pos, scoreObj);
		}
	}
	
	public void renderScoreboard(ScreenPosition pos, ScoreObjective scoreObjective) {
		net.minecraft.scoreboard.Scoreboard scoreboard = scoreObjective.getScoreboard();
		Collection<Score> scores = scoreboard.getSortedScores(scoreObjective);
		List<Score> filteredScores = Lists.newArrayList(Iterables.filter(scores, new Predicate<Score>() {
			public boolean apply(Score score) {
				return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
			}
		}));
		
		if (filteredScores.size() > 15) {
			scores = Lists.newArrayList(Iterables.skip(filteredScores, scores.size() - 15));
		} else {
			scores = filteredScores;
		}
		
		int i1 = (scores.size() + 1) * font.FONT_HEIGHT;
		int j1 = i1 + 1;
		
		if (pos.getRelativeY() < 1.0D / 3.0D) {
			j1 += pos.getAbsoluteY();
		} else if (pos.getRelativeY() > 2.0D / 3.0D) {
			j1 += pos.getAbsoluteY() + getHeight() - i1;
		} else {
			j1 += pos.getAbsoluteY() + getHeight() / 2 - i1 / 2;
		}
		
		int i = font.getStringWidth(scoreObjective.getDisplayName());
		
		for (Score score : scores) {
			ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
			String lineText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName()) + (castOptionValueIntoBoolean("showNumbers") ? ": " + score.getScorePoints() : "");
			
			i = Math.max(i, font.getStringWidth(lineText));
		}
		
		int l1;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			l1 = pos.getAbsoluteX() + 1;
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			l1 = pos.getAbsoluteX() + getWidth() - i + 1;
		} else {
			l1 = pos.getAbsoluteX() + getWidth() / 2 - i / 2 + 1;
		}
		
		int j = 0;
		
		for (Score score : scores) {
			j++;
			
			int k = j1 - j * font.FONT_HEIGHT;
			int l = l1 + i;
			
			Gui.drawRect(l1 - 2, k, l, k + font.FONT_HEIGHT, getOptionColor("backgroundColor").getARGB());
			
			ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
			String lineText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName());
			
			drawText(lineText, l1, k, ColorManager.WHITE.getARGB(), castOptionValueIntoBoolean("textShadow"), false);
			
			if (castOptionValueIntoBoolean("showNumbers")) {
				String number = String.valueOf(score.getScorePoints());
				
				drawText(number, l - font.getStringWidth(number), k, getOptionColor("numbersColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("numbersColor").isChromaEnabled());
			}
			
			if (j == scores.size()) {                
				Gui.drawRect(l1 - 2 , k - font.FONT_HEIGHT - 1, l, k - 1, getOptionColor("headerBackgroundColor").getARGB());
				Gui.drawRect(l1 - 2, k - 1, l, k, getOptionColor("backgroundColor").getARGB());
				
				String title = scoreObjective.getDisplayName();
				
				drawText(title, l1 + i / 2 - font.getStringWidth(title) / 2, k - font.FONT_HEIGHT, ColorManager.WHITE.getARGB(), castOptionValueIntoBoolean("textShadow"), false);
			}
		}
	}
	
	private ScoreObjective getDummyScoreObjective() {
		net.minecraft.scoreboard.Scoreboard scoreboard = new net.minecraft.scoreboard.Scoreboard();
		ScoreObjective scoreObj = scoreboard.addScoreObjective("dummy", IScoreObjectiveCriteria.DUMMY);
		
		scoreObj.setDisplayName("PIXEL CLIENT");
		scoreboard.getValueFromObjective("Born", scoreObj).setScorePoints(2);
		scoreboard.getValueFromObjective("Again!", scoreObj).setScorePoints(1);
		
		return scoreObj;
	}
}