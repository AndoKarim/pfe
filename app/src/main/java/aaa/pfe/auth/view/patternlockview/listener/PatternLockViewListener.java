package aaa.pfe.auth.view.patternlockview.listener;

/**
 * Created by aritraroy on 19/03/17.
 */

import java.util.List;

import aaa.pfe.auth.view.patternlockview.PatternLockView;

/**
 * The callback interface for detecting patterns entered by the user
 */
public interface PatternLockViewListener {

    /**
     * Fired when the pattern drawing has just started
     */
    void onStarted();

    /**
     * Fired when the pattern is still being drawn and progressed to
     */
    void onProgress(List<PatternLockView.Dot> progressPattern);

    /**
     * Fired when the user has completed drawing the pattern and has moved their finger away
     * from the view
     */
    void onComplete(List<PatternLockView.Dot> pattern, boolean sizeReached);

    /**
     * Fired when the patten has been cleared from the view
     */
    void onCleared();

    /**
     * Fired when the pattern max size is reached
     */
    void onSizeReached(List<PatternLockView.Dot> pattern);
}