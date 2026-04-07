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
		
		int totalHeight = (scores.size() + 1) * font.FONT_HEIGHT;
		int rectTop = totalHeight + 1;
		
		if (pos.getRelativeY() < 1.0D / 3.0D) {
			rectTop += pos.getAbsoluteY();
		} else if (pos.getRelativeY() > 2.0D / 3.0D) {
			rectTop += pos.getAbsoluteY() + getHeight() - totalHeight;
		} else {
			rectTop += pos.getAbsoluteY() + getHeight() / 2 - totalHeight / 2;
		}
		
		int maxWidth = font.getStringWidth(scoreObjective.getDisplayName());
		
		for (Score score : scores) {
			ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
			String lineText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName()) + (castOptionValueIntoBoolean("showNumbers") ? ": " + score.getScorePoints() : "");
			
			maxWidth = Math.max(maxWidth, font.getStringWidth(lineText));
		}
		
		int rectLeft;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			rectLeft = pos.getAbsoluteX() + 1;
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			rectLeft = pos.getAbsoluteX() + getWidth() - maxWidth + 1;
		} else {
			rectLeft = pos.getAbsoluteX() + getWidth() / 2 - maxWidth / 2 + 1;
		}
		
		int rectRight = rectLeft + maxWidth;
		
		int i = 0;
		
		for (Score score : scores) {
			i++;
			
			int top = rectTop - i * font.FONT_HEIGHT;
			
			Gui.drawRect(rectLeft - 2, top, rectRight, top + font.FONT_HEIGHT, getOptionColor("backgroundColor").getARGB());
			
			ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
			String lineText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName());
			
			drawText(lineText, rectLeft, top, ColorManager.WHITE.getARGB(), castOptionValueIntoBoolean("textShadow"), false);
			
			if (castOptionValueIntoBoolean("showNumbers")) {
				String number = String.valueOf(score.getScorePoints());
				
				drawText(number, rectRight - font.getStringWidth(number), top, getOptionColor("numbersColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("numbersColor").isChromaEnabled());
			}
			
			if (i == scores.size()) {                
				Gui.drawRect(rectLeft - 2 , top - font.FONT_HEIGHT - 1, rectRight, top - 1, getOptionColor("headerBackgroundColor").getARGB());
				Gui.drawRect(rectLeft - 2, top - 1, rectRight, top, getOptionColor("backgroundColor").getARGB());
				
				String title = scoreObjective.getDisplayName();
				
				drawText(title, rectLeft + maxWidth / 2 - font.getStringWidth(title) / 2, top - font.FONT_HEIGHT, ColorManager.WHITE.getARGB(), castOptionValueIntoBoolean("textShadow"), false);
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