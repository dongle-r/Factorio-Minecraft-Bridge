
local function ONLOAD()
    global.transferChests = global.transferChests or {}
end

local function ONBUILD( event )
    local entity = event.created_entity
    if entity.name == "transfer-chest" then
        local surface = entity.surface
        local force = entity.force
        newTransfer = surface.create_entity{ name = "transfer-chest", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferChests, newTransfer )
    end
end

local function ONREMOVE( event )
    local entity = event.entity
    if entity.name == "transfer-chest" then
        for index, l in pairs( global.transferChests ) do
            if entity == l then
                global.transferChests[index] = nil
                break
            end
        end
    end
end

script.on_init(ONLOAD)

script.on_event( defines.events.on_built_entity, ONBUILD )
script.on_event( defines.events.on_robot_built_entity, ONBUILD )
script.on_event( defines.events.on_pre_player_mined_item, ONREMOVE )
script.on_event( defines.events.on_robot_pre_mined, ONREMOVE )
script.on_event( defines.events.on_entity_died, ONREMOVE )


--[[
    Transfer Chest, write to the file
]]
script.on_event({defines.events.on_tick}, 
    function (e)
        if e.tick % 60 == 0 then
            local saveString = ""
            for k, transfer in pairs (global.transferChests) do
                if transfer.valid then
                    local inventory = transfer.get_inventory(defines.inventory.chest)
                    if not inventory.is_empty() then
                        saveString = saveString .. inventory[1].name .. " : " .. inventory[1].count .. "\n"
                    end
                end
            end
            game.write_file("toMC.dat", saveString)
        end
    end
)

--[[
    Transfer Chest, read from file. Things probably shouldnt be inserted here
]]

