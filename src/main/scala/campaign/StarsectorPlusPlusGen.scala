package r2u.starsectorplusplus.campaign

import scala.annotation.*

import java.util.Random

import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.*
import com.fs.starfarer.api.campaign.econ.*

import r2u.starsectorplusplus.campaign.systems.*
import scala.compiletime.ops.double

class StarsectorPlusPlusGen:
    def generate(sector:SectorAPI):Unit =
        Hazog()
            .generate(sector)

object StarsectorPlusPlusGen:
    def addJumpPoint(id:String, name:String, star:PlanetAPI, orbitDistance:Float, system:StarSystemAPI):Unit =
        val jumpPoint:JumpPointAPI = Global.getFactory()
            .createJumpPoint(id, name)

        jumpPoint.setCircularOrbit(
            star,
            2,
            orbitDistance,
            orbitDistance / 20 + Random()
                .nextFloat() * 5
        )
        jumpPoint.setStandardWormholeToHyperspaceVisual()
        system.addEntity(jumpPoint)

    def addStableLocation(system:StarSystemAPI, id:String, name:String, locationType:String, faction:String, orbitDistance:Float):Unit =
        system.addCustomEntity(id, name, locationType, faction)
            .setCircularOrbit(
                system.getStar(),
                2,
                orbitDistance,
                orbitDistance / 20 + Random()
                    .nextFloat() * 5
            )

    def addMarket(
        factionId:String, 
        primaryEntity:SectorEntityToken, 
        connectedEntities:Option[Array[SectorEntityToken]], 
        name:String, 
        size:Int, 
        conditions:Array[String],
        submarkets:Array[String],
        industries:Array[String],
        tariff:Float,
        isFreePort:Boolean,
        floatyJunk:Boolean
    ):MarketAPI =
        val globalEconomy:EconomyAPI = Global.getSector()
            .getEconomy()

        val marketId:String = s"${primaryEntity.getId()}_market"
        val newMarket:MarketAPI = Global.getFactory()
            .createMarket(marketId, name, size)

        newMarket.setFactionId(factionId)
        newMarket.setPrimaryEntity(primaryEntity)
        newMarket.getTariff()
            .setBaseValue(tariff)

        doSubmarkets(0, newMarket, submarkets)

        doConditions(0, newMarket, conditions)

        doIndustries(0, newMarket, industries)

        newMarket.setFreePort(isFreePort)

        globalEconomy.addMarket(newMarket, floatyJunk)

        primaryEntity.setMarket(newMarket)
        primaryEntity.setFaction(factionId)

        if connectedEntities.nonEmpty then
            doConnectedEntities(0, newMarket, factionId, connectedEntities.get)

        newMarket

    @tailrec
    private def doSubmarkets(idx:Int, market:MarketAPI, submarkets:Array[String]):Unit = 
        if idx < submarkets.length then
            market.addSubmarket(submarkets(idx))
            val nextIdx:Int = idx + 1

            doSubmarkets(nextIdx, market, submarkets)

    @tailrec
    private def doConditions(idx:Int, market:MarketAPI, conditions:Array[String]):Unit = 
        if idx < conditions.length then
            market.addCondition(conditions(idx))
            val nextIdx:Int = idx + 1

            doConditions(nextIdx, market, conditions)

    @tailrec
    private def doIndustries(idx:Int, market:MarketAPI, industries:Array[String]):Unit = 
        if idx < industries.length then
            market.addIndustry(industries(idx))
            val nextIdx:Int = idx + 1

            doIndustries(nextIdx, market, industries)

    @tailrec
    private def doConnectedEntities(idx:Int, market:MarketAPI, factionId:String, connectedEntities:Array[SectorEntityToken]):Unit =
        if idx < connectedEntities.length then
            market.getConnectedEntities()
                .add(connectedEntities(idx))

            connectedEntities(idx).setMarket(market)
            connectedEntities(idx).setFaction(factionId)
            val nextIdx:Int = idx + 1

            doConnectedEntities(nextIdx, market, factionId, connectedEntities)