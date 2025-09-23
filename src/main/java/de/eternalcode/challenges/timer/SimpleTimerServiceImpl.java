package de.eternalcode.challenges.timer;

import de.eternalcode.challenges.api.services.TimerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class SimpleTimerServiceImpl implements TimerService {

    private static final Logger log = Logger.getLogger(SimpleTimerServiceImpl.class.getSimpleName());

    private int time = 0;
    private boolean running = false;
    private int taskId = -1;

    private final Plugin plugin;

    public SimpleTimerServiceImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        if (running) return;
        running = true;
        Bukkit.getScheduler().cancelTask(taskId);

        timerTask();
        /*
        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                time++;
                updateDisplay();
            }
        }.runTaskTimer(plugin, 20, 20).getTaskId();

         */
    }

    @Override
    public void pause() {
        if (!running) return;
        running = false;
        /*
        Bukkit.getScheduler().cancelTask(taskId);

        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                updateDisplay();
            }
        }.runTaskTimer(plugin, 20, 20).getTaskId();

         */
        timerTask();
    }

    @Override
    public void reset() {
        pause();
        time = 0;
        updateDisplay();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getElapsedTime() {
        return time;
    }

    @Override
    public void setElapsedTime(int timeInSeconds) {
        this.time = timeInSeconds;
        updateDisplay();
    }

    @Override
    public String getFormattedTime() {
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void updateDisplay() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            showActionBar(player);
        }
    }

    @Override
    public void showActionBar(@NotNull Player player) {
        if(!running) {
            player.sendActionBar(Component.text("§cTimer paused"));
            return;
        }
        player.sendActionBar(Component.text("§eTimer: §f" + getFormattedTime()));
    }

    public void timerTask () {
        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                if(running)
                    time++;
                updateDisplay();
            }
        }.runTaskTimer(plugin, 20, 20).getTaskId();
    }
}
