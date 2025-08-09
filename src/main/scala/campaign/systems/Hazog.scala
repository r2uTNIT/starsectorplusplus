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
    val bleckleyDistance:Float = 4500 // volcanic
    val citadelHazogDistance:Float = 1000 // military base
    val cochranDistance:Float = 12700 // gas giant
    val cochranRingDistance:Float = 600
    val jumpPointInnerDistance:Float = 3000
    val jumpPointOuterDistance:Float = 12000
    val jumpPointFringeDistance:Float = 23000
    val caryDistance:Float = 22700 // barren
    val payneDistance:Float = 17025  // asteroid belt
    val stableLocation1Distance:Float = 8000
    val stableLocation2Distance:Float = 15000
    val stableLocation3Distance:Float = 20000
    val payneAstropolisDistance:Float = 400

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
            bleckleyDistance,
            bleckleyDistance / 20 + Random()
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
                Industries.GROUNDDEFENSES
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
            caryDistance,
            caryDistance / 20 + Random()
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
                Industries.PATROLHQ
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
            cochranDistance,
            cochranDistance / 20 + Random()
                .nextFloat() * 5
        )
        val cochran_market:MarketAPI = cochran.getMarket()
        cochran_market.addCondition(Conditions.HIGH_GRAVITY)
        cochran_market.addCondition(Conditions.DENSE_ATMOSPHERE)
        cochran_market.addCondition(Conditions.VOLATILES_TRACE)

        val citadelHazog:CustomCampaignEntityAPI = system.addCustomEntity("citadel_hazog", "Citadel Hazog", "station_hightech1", Factions.INDEPENDENT)
        citadelHazog.setCircularOrbitPointingDown(
            cochran,
            0,
            citadelHazogDistance,
            citadelHazogDistance / 20 + Random()
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
        val payneAstropolis:CustomCampaignEntityAPI = system.addCustomEntity("payne_astropolis", "Payne Astropolis", "station_lowtech2", Factions.INDEPENDENT)
        payneAstropolis.setCircularOrbitPointingDown(bleckley, 0, payneAstropolisDistance, payneAstropolisDistance / 20 + Random().nextFloat() * 5)
        StarsectorPlusPlusGen.addMarket(
            Factions.INDEPENDENT,
            payneAstropolis,
            None,
            "Payne Astropolis",
            3,
            Array(
                Conditions.POPULATION_3
            ),
            Array(
                Submarkets.SUBMARKET_STORAGE,
                Submarkets.SUBMARKET_OPEN,
                Submarkets.SUBMARKET_BLACK
            ),
            Array(
                Industries.POPULATION,
                Industries.SPACEPORT,
                Industries.FUELPROD,
                Industries.PATROLHQ
            ),
            0.25,
            false,
            false
        )
        system.addAsteroidBelt(
            star, 
            1000, 
            payneDistance, 
            500,
            (payneDistance / 20 + Random().nextFloat() * 5) * 0.25F,
            payneDistance / 20 + Random().nextFloat() * 5,
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
            payneDistance,
            payneDistance / 20 + Random()
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
            cochranRingDistance,
            cochranRingDistance / 20 + Random()
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
            cochranRingDistance + 100,
            (cochranRingDistance + 100) / 20 + Random()
                .nextFloat() * 5,
            Terrain.RING,
            "Cochran's Disc"
        )
        StarsectorPlusPlusGen.addStableLocation(system, "hazogStableLocation1", "Hazog Relay", Entities.COMM_RELAY_MAKESHIFT, Factions.INDEPENDENT, stableLocation1Distance)
        StarsectorPlusPlusGen.addStableLocation(system, "hazogStableLocation2", "Makeshift Sensor Array", Entities.SENSOR_ARRAY_MAKESHIFT, Factions.INDEPENDENT, stableLocation2Distance)
        StarsectorPlusPlusGen.addStableLocation(system, "hazogStableLocation3", "Makeshift Nav Buoy", Entities.NAV_BUOY_MAKESHIFT, Factions.PERSEAN, stableLocation3Distance)

        StarsectorPlusPlusGen.addJumpPoint("inner_jump_point", "Inner System Jump Point", star, jumpPointInnerDistance, system)
        StarsectorPlusPlusGen.addJumpPoint("outer_jump_point", "Outer System Jump Point", star, jumpPointOuterDistance, system)
        StarsectorPlusPlusGen.addJumpPoint("fringe_jump_point", "Fringe Jump Point", star, jumpPointFringeDistance, system)

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