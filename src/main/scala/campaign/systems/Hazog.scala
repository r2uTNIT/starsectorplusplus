package r2u.starsectorplusplus.campaign.systems

import java.awt.Color
import java.util.Random
import java.util.concurrent.locks.*

import com.fs.starfarer.api.*
import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.campaign.econ.*
import com.fs.starfarer.api.characters.*
import com.fs.starfarer.api.impl.campaign.ids.*
import com.fs.starfarer.api.impl.campaign.procgen.*
import com.fs.starfarer.api.impl.campaign.terrain.*
import com.fs.starfarer.api.util.*

import r2u.starsectorplusplus.campaign.*

class Hazog:
    final val BleckleyDistance:Float = 4500 // volcanic
    final val CitadelHazogDistance:Float = 1000 // military base
    final val CochranDistance:Float = 12700 // gas giant
    final val CochranRingDistance:Float = 600
    final val JumpPointInnerDistance:Float = 3000
    final val JumpPointOuterDistance:Float = 12000
    final val JumpPointFringeDistance:Float = 23000
    final val CaryDistance:Float = 22700 // barren
    final val PayneDistance:Float = 17025  // asteroid belt
    final val StableLocation1Distance:Float = 8000
    final val StableLocation2Distance:Float = 15000
    final val StableLocation3Distance:Float = 20000

    def generate(sector:SectorAPI):Unit =
        val system:StarSystemAPI = sector.createStarSystem("Hazog")
        system.getLocation()
            .set(5000, 2500)

        system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg")
        val star:PlanetAPI = system.initStar("Hazog", "star_red_supergiant", 1800, 5000, 2500, 1800)
        
        system.setLightColor(
            Color(239, 155, 128)
        )
        val bleckley:PlanetAPI = system.addPlanet(
            "bleckley", 
            star, 
            "Bleckley", 
            "lava_minor", 
            360 * Math.random().asInstanceOf[Float],
            150,
            BleckleyDistance,
            BleckleyDistance / 20 + Random()
                .nextFloat() * 5
        )
        StarsectorPlusPlusGen.addMarket(
            Factions.INDEPENDENT,
            bleckley,
            None,
            "Bleckley",
            5,
            Array(
                Conditions.POPULATION_5,
                Conditions.ORE_ULTRARICH,
                Conditions.RARE_ORE_ULTRARICH,
                Conditions.TOXIC_ATMOSPHERE,
                Conditions.VERY_HOT,
                Conditions.EXTREME_TECTONIC_ACTIVITY
            ),
            Array(
                Submarkets.SUBMARKET_STORAGE,
                Submarkets.SUBMARKET_OPEN,
                Submarkets.SUBMARKET_BLACK
            ),
            Array(
                Industries.POPULATION,
                Industries.MEGAPORT,
                Industries.MINING,
                Industries.REFINING,
                Industries.HEAVYINDUSTRY,
                Industries.PATROLHQ,
                Industries.GROUNDDEFENSES,
                Industries.ORBITALSTATION
            ),
            0.25,
            false,
            true
        )
        val cary:PlanetAPI = system.addPlanet(
            "cary",
            star,
            "Cary",
            "barren",
            360 * Math.random().asInstanceOf[Float],
            100,
            CaryDistance,
            CaryDistance / 20 + Random()
                .nextFloat() * 5
        )
        StarsectorPlusPlusGen.addMarket(
            Factions.PERSEAN,
            cary,
            None,
            "Cary",
            3,
            Array(
                Conditions.POPULATION_3,
                Conditions.VERY_COLD,
                Conditions.ORE_SPARSE,
                Conditions.RARE_ORE_MODERATE
            ),
            Array(
                Submarkets.SUBMARKET_STORAGE,
                Submarkets.SUBMARKET_OPEN,
                Submarkets.SUBMARKET_BLACK
            ),
            Array(
                Industries.POPULATION,
                Industries.SPACEPORT,
                Industries.MINING,
                Industries.GROUNDDEFENSES
            ),
            0.25,
            true,
            true
        )
        val cochran:PlanetAPI = system.addPlanet(
            "cochran",
            star,
            "Cochran",
            "gas_giant",
            360 * Math.random().asInstanceOf[Float],
            400,
            CochranDistance,
            CochranDistance / 20 + Random()
                .nextFloat() * 5
        )
        val cochran_market:MarketAPI = cochran.getMarket()
        cochran_market.addCondition(Conditions.HIGH_GRAVITY)
        cochran_market.addCondition(Conditions.DENSE_ATMOSPHERE)
        cochran_market.addCondition(Conditions.VOLATILES_TRACE)

        val citadelHazog:CustomCampaignEntityAPI = system.addCustomEntity("citadel_hazog", "Citadel Hazog", "station_lowtech2", Factions.INDEPENDENT)
        citadelHazog.setCircularOrbitPointingDown(
            cochran,
            0,
            CitadelHazogDistance,
            CitadelHazogDistance / 20 + Random()
                .nextFloat() * 5
        )
        StarsectorPlusPlusGen.addMarket(
            Factions.INDEPENDENT,
            citadelHazog,
            None,
            "Citadel Hazog",
            4,
            Array(
                Conditions.POPULATION_4
            ),
            Array(
                Submarkets.SUBMARKET_STORAGE,
                Submarkets.SUBMARKET_OPEN,
                Submarkets.SUBMARKET_BLACK
            ),
            Array(
                Industries.POPULATION,
                Industries.SPACEPORT,
                Industries.LIGHTINDUSTRY,
                Industries.HEAVYBATTERIES,
                Industries.HIGHCOMMAND
            ),
            0.25,
            false,
            false
        )
        system.addAsteroidBelt(
            star, 
            1000, 
            PayneDistance, 
            500,
            (PayneDistance / 20 + Random()
                .nextFloat() * 5) * 0.25F,
            PayneDistance / 20 + Random()
                .nextFloat() * 5,
            Terrain.ASTEROID_BELT,
            "Ring of Payne" 
        )
        system.addRingBand(
            star,
            "misc",
            "rings_asteroids0",
            256,
            0,
            Color.gray,
            256,
            PayneDistance,
            PayneDistance / 20 + Random()
                .nextFloat() * 5 
        )
        system.addRingBand(
            cochran,
            "misc",
            "rings_special0",
            256,
            1,
            Color.ORANGE,
            256,
            CochranRingDistance,
            CochranRingDistance / 20 + Random()
                .nextFloat() * 5,
            Terrain.RING,
            "Cochran's Disc"
        )
        system.addRingBand(
            cochran,
            "misc",
            "rings_special0",
            256,
            2,
            Color.ORANGE,
            256,
            CochranRingDistance + 100,
            (CochranRingDistance + 100) / 20 + Random()
                .nextFloat() * 5,
            Terrain.RING,
            "Cochran's Disc"
        )
        StarsectorPlusPlusGen.addStableLocation(system, "hazogStableLocation1", "Hazog Relay", Entities.COMM_RELAY_MAKESHIFT, Factions.INDEPENDENT, StableLocation1Distance)
        StarsectorPlusPlusGen.addStableLocation(system, "hazogStableLocation2", "Makeshift Sensor Array", Entities.SENSOR_ARRAY_MAKESHIFT, Factions.INDEPENDENT, StableLocation2Distance)
        StarsectorPlusPlusGen.addStableLocation(system, "hazogStableLocation3", "Makeshift Nav Buoy", Entities.NAV_BUOY_MAKESHIFT, Factions.PERSEAN, StableLocation3Distance)

        StarsectorPlusPlusGen.addJumpPoint("inner_jump_point", "Inner System Jump Point", star, JumpPointInnerDistance, system)
        StarsectorPlusPlusGen.addJumpPoint("outer_jump_point", "Outer System Jump Point", star, JumpPointOuterDistance, system)
        StarsectorPlusPlusGen.addJumpPoint("fringe_jump_point", "Fringe Jump Point", star, JumpPointFringeDistance, system)

        system.autogenerateHyperspaceJumpPoints(true, false)
        
        NebulaEditor(
            Misc.getHyperspaceTerrainPlugin()
        ).clearArc(
            system.getLocation().x,
            system.getLocation().y,
            0,
            system.getMaxRadiusInHyperspace(),
            0,
            360
        )