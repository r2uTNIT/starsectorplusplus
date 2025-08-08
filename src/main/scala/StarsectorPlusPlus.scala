package r2u.starsectorplusplus

import com.fs.starfarer.api.*

import r2u.starsectorplusplus.campaign.*

// scala.collection causes "File access and reflection are not allowed to scripts" error, use Java collections (or Array[T]) instead!
// btw for loops call methods of scala.collection under the hood!

class StarsectorPlusPlus extends BaseModPlugin:
    override def onNewGame():Unit = 
        StarsectorPlusPlusGen.generate(Global.getSector())