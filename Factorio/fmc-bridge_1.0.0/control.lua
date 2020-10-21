require "util"
local function ONLOAD()
    global.transferChests = global.transferChests or {}
    global.transferChestsData = global.transferChestsData or {}
    global.transferChestsPriority = global.transferChestsPriority or {}
    
    global.transferTanks = global.transferTanks or {}
    global.transferTanksData = global.transferTanksData or {}
    global.transferTanksPriority = global.transferTanksPriority or {}

    global.transferAccumulators = global.transferAccumulators or {}
    global.transferAccumulatorsData = global.transferAccumulatorsData or {}
    global.transferAccumulatorsPriority = global.transferAccumulatorsPriority or {}

    global.priorities = global.priorities or {chest=-1,tank=-1,accumulator=-1}

    global.sendItems = global.sendItems or {}
    global.sendItems_Buffer = global.sendItems_Buffer or {}

    global.sendFluids = global.sendFluids or {}
    global.sendFluids_Buffer = global.sendFluids_Buffer or {}

    global.sendEnergy = global.sendEnergy or {}
    global.sendEnergy_Buffer = global.sendEnergy_Buffer or {}
end

local function ONBUILD( event )
    local entity = event.created_entity
    if entity.name == "send-chest" then
        local surface = entity.surface
        local force = entity.force
        newSendChest = surface.create_entity{ name = "send-chest", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferChests, newSendChest )

        _data = {filter="", priority=0}
        chestData = {entity=newSendChest, data=_data}
        table.insert(global.transferChestsData, chestData)
    elseif entity.name == "receive-chest" then
        local surface = entity.surface
        local force = entity.force
        newReceiveChest = surface.create_entity{ name = "receive-chest", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferChests, newReceiveChest )

        _data = {filter="", priority=0}
        chestData = {entity=newReceiveChest, data=_data}
        table.insert(global.transferChestsData, chestData)
    elseif entity.name == "send-tank" then
        local surface = entity.surface
        local force = entity.force
        newSendTank = surface.create_entity{ name = "send-tank", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferTanks, newSendTank )

        _data = {filter="", priority=0}
        tankData = {entity=newSendTank, data=_data}
        table.insert(global.transferTanksData, tankData)
    elseif entity.name == "receive-tank" then
        local surface = entity.surface
        local force = entity.force
        newReceiveTank = surface.create_entity{ name = "receive-tank", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferTanks, newReceiveTank )

        _data = {filter="", priority=0}
        tankData = {entity=newReceiveTank, data=_data}
        table.insert(global.transferTanksData, tankData)
    elseif entity.name == "send-accumulator" then
        local surface = entity.surface
        local force = entity.force
        newSendAccumulator = surface.create_entity{ name = "send-accumulator", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferAccumulators, newSendAccumulator )

        _data = {filter="", priority=0}
        accumulatorData = {entity=newSendAccumulator, data=_data}
        table.insert(global.transferAccumulatorsData, accumulatorData)
    elseif entity.name == "receive-accumulator" then
        local surface = entity.surface
        local force = entity.force
        newReceiveAccumulator = surface.create_entity{ name = "receive-accumulator", position = entity.position, force = force }
        entity.destroy()
        table.insert( global.transferAccumulators, newReceiveAccumulator )

        _data = {filter="", priority=0}
        accumulatorData = {entity=newReceiveAccumulator, data=_data}
        table.insert(global.transferAccumulatorsData, accumulatorData)
    end
end


local function ONREMOVE( event )
    local entity = event.entity
    if entity.name == "send-chest" then
        for index, l in pairs( global.transferChests ) do
            if entity == l then
                global.transferChests[index] = nil
                global.transferChestsData[index] = nil
                break
            end
        end
    elseif entity.name == "receive-chest" then
        for index, l in pairs( global.transferChests ) do
            if entity == l then
                global.transferChests[index] = nil
                global.transferChestsData[index] = nil
                break
            end
        end
    elseif entity.name == "send-tank" then
        for index, l in pairs( global.transferTanks ) do
            if entity == l then
                global.transferTanks[index] = nil
                global.transferTanksData[index] = nil
                break
            end
        end
    elseif entity.name == "receive-tank" then
        for index, l in pairs( global.transferTanks ) do
            if entity == l then
                global.transferTanks[index] = nil
                global.transferTanksData[index] = nil
                break
            end
        end
    elseif entity.name == "send-accumulator" then
        for index, l in pairs( global.transferAccumulators ) do
            if entity == l then
                global.transferAccumulators[index] = nil
                global.transferAccumulatorsData[index] = nil
                break
            end
        end
    elseif entity.name == "receive-accumulator" then
        for index, l in pairs( global.transferAccumulators ) do
            if entity == l then
                global.transferAccumulators[index] = nil
                global.transferAccumulatorsData[index] = nil
                break
            end
        end
    end
end

-- Getters / Setters for Chest Data
local function get_chest_data(e)
    for k, chest in pairs (global.transferChestsData) do
        if chest.entity == e then
            return chest.data
        end
    end
end

local function set_chest_data(e, _data)
    for k, chest in pairs (global.transferChestsData) do
        if chest.entity == e then
            chest.data = _data
        end
    end
end

-- Getters / Setters for Tank Data
local function get_tank_data(e)
    for k, tank in pairs (global.transferTanksData) do
        if tank.entity == e then
            return tank.data
        end
    end
end

local function set_tank_data(e, _data)
    for k, tank in pairs (global.transferTanksData) do
        if tank.entity == e then
            tank.data = _data
        end
    end
end

-- Getters / Setters for Accumulator Data
local function get_accumulator_data(e)
    for k, accumulator in pairs (global.transferAccumulatorsData) do
        if accumulator.entity == e then
            return accumulator.data
        end
    end
end

local function set_accumulator_data(e, _data)
    for k, accumulator in pairs (global.transferAccumulatorsData) do
        if accumulator.entity == e then
            accumulator.data = _data
        end
    end
end

local function get_chest_index(e)
    local index = 0
    for k, chest in pairs (global.transferChests) do
        if chest == e then
            return index
        end
        index = index + 1
    end
end

local function get_tank_index(e)
    local index = 0
    for k, tank in pairs (global.transferTanks) do
        if tank == e then
            return index
        end
        index = index + 1
    end
end

local function get_accumulator_index (e)
    local index = 0
    for k, accumulator in pairs (global.transferAccumulators) do
        if accumulator == e then
            return index
        end
        index = index + 1
    end
end


local function is_fmc_entity(e)
    return e == "send-chest" or e == "receive-chest" or e == "send-tank" or e == "receive-tank" or e == "send-accumulator" or e == "receive-accumulator"
end

local ui_open = false

local function build_gui(frame, type, data)
    if type == "chest" then
        local prio_flow = frame.add{type="flow",name="fmc-priority-flow",direction="horizontal"}
        local filter_flow = frame.add{type="flow",name="fmc-filter-flow",direction="horizontal"}
        prio_flow.add{type="label",caption="Priority:"}
        prio_flow.add{type="textfield",name="fmc-priority",text=data.priority,numeric=true,allow_decimal=false,allow_negative=false}
        filter_flow.add{type="label",caption="Filter:"}
        filter_flow.add{type="choose-elem-button", name="fmc-choose-item",elem_type="item"}
        if data.filter ~= "" then 
            filter_flow["fmc-choose-item"].elem_value=data.filter
        end
        return frame
    elseif type == "tank" then
        local prio_flow = frame.add{type="flow",name="fmc-priority-flow",direction="horizontal"}
        local filter_flow = frame.add{type="flow",name="fmc-filter-flow",direction="horizontal"}
        prio_flow.add{type="label",caption="Priority:"}
        prio_flow.add{type="textfield",name="fmc-priority",text=data.priority,numeric=true,allow_decimal=false,allow_negative=false}
        filter_flow.add{type="label",caption="Filter:"}
        filter_flow.add{type="choose-elem-button", name="fmc-choose-item",elem_type="fluid"}
        if data.filter ~= "" then 
            filter_flow["fmc-choose-item"].elem_value=data.filter
        end
        return frame
    elseif type == "accumulator" then
        local prio_flow = frame.add{type="flow",name="fmc-priority-flow",direction="horizontal"}
        prio_flow.add{type="label",caption="Priority:"}
        prio_flow.add{type="textfield",name="fmc-priority",text=data.priority,numeric=true,allow_decimal=false,allow_negative=false}
        return frame
    end
end

local function gui_opened( event )
    if event.entity ~= nil then
        local _gui = game.players[event.player_index].gui.top
        if event.entity.name == "receive-chest" then
            data = get_chest_data(event.entity)
            local _frame = _gui.add{type="frame", name="fmc-frame",caption="FMC Configure Panel", direction="vertical"}
            build_gui(_frame, "chest", data)
            ui_open = true
        elseif event.entity.name == "receive-tank" then
            data = get_tank_data(event.entity)
            local _frame = _gui.add{type="frame", name="fmc-frame",caption="FMC Configure Panel", direction="vertical"}
            build_gui(_frame, "tank", data)
            ui_open = true
        elseif event.entity.name == "receive-accumulator" then
            data = get_accumulator_data(event.entity)
            local _frame = _gui.add{type="frame", name="fmc-frame",caption="FMC Configure Panel", direction="vertical"}
            build_gui(_frame, "accumulator", data)
            ui_open = true
        end
    end
end

local function gui_closed (event)
    if event.entity ~= nil then
        local _gui = game.players[event.player_index].gui.top
        local _frame
        local prio_flow
        local filter_flow
        if is_fmc_entity(event.entity.name) then
            _frame = _gui["fmc-frame"]
            prio_flow = _frame["fmc-priority-flow"]
            filter_flow = _frame["fmc-filter-flow"]
            if event.entity.name == "receive-chest" then
                local data = {priority=tonumber(prio_flow["fmc-priority"].text),filter=filter_flow["fmc-choose-item"].elem_value}
                set_chest_data(event.entity, data)
                if data.priority > 0 then
                    table.insert(global.transferChests, data.priority, table.remove(global.transferChests,get_chest_index(event.entity)))
                end
            elseif event.entity.name == "receive-tank" then
                local data = {priority=tonumber(prio_flow["fmc-priority"].text),filter=filter_flow["fmc-choose-item"].elem_value}
                set_tank_data(event.entity, data)
                if data.priority > 0 then
                    table.insert(global.transferTanks, data.priority, table.remove(global.transferTanks,get_tank_index(event.entity)))
                end
            elseif event.entity.name == "receive-accumulator" then
                local data = {priority=tonumber(prio_flow["fmc-priority"].text)}
                set_accumulator_data(event.entity, data)
                if data.priority > 0 then
                    table.insert(global.transferAccumulators, data.priority, table.remove(global.transferAccumulators,get_accumulator_index(event.entity)))
                end
            end
            ui_open = false
            _gui["fmc-frame"].destroy()
        end
    end
end

script.on_init(ONLOAD)

commands.add_command("itemData", {"cmd.find-item"}, function(event)
    local player = game.players[event.player_index]
    if player.cursor_stack.valid_for_read then
        player.print(player.cursor_stack.name)
    else
        player.print("You need to be holding an item!")
    end
end)

--script.on_init(ONLOADREC)

script.on_event( defines.events.on_built_entity, ONBUILD )
script.on_event( defines.events.on_robot_built_entity, ONBUILD )
script.on_event( defines.events.on_pre_player_mined_item, ONREMOVE )
script.on_event( defines.events.on_robot_pre_mined, ONREMOVE )
script.on_event( defines.events.on_entity_died, ONREMOVE )
script.on_event( defines.events.on_gui_opened, gui_opened)
script.on_event( defines.events.on_gui_closed, gui_closed)

--[[
    Send Chest, write to the file
]]
script.on_event({defines.events.on_tick}, 
    function (e)
        if e.tick % 60 == 0  and ui_opened == false then
            if(global.sendItems_Buffer == nil) then
                global.sendItems_Buffer = global.sendItems_Buffer or {}
            end
            if(global.sendFluids_Buffer == nil) then
                global.sendFluids_Buffer = global.sendFluids_Buffer or {}
            end
            if(global.sendEnergy_Buffer == nil) then
                global.sendEnergy_Buffer = global.sendEnergy_Buffer or {}
            end

            for k, send in pairs (global.transferChests) do
                if send.name == "send-chest" then
                    local inventory = send.get_inventory(defines.inventory.chest)
                    if not inventory.is_empty() then
                        if (global.sendItems_Buffer[inventory[1].name] == nil) then
                            global.sendItems_Buffer[inventory[1].name] = 0
                        end
                        global.sendItems_Buffer[inventory[1].name] = global.sendItems_Buffer[inventory[1].name] + inventory[1].count
                        inventory.clear();
                    end
                end
            end
            for k, send in pairs (global.transferTanks) do
                if send.name == "send-tank" then
                    fluid = send.fluidbox[1]
                    if fluid == nil then
                        -- do nothing, we just need to check if its nil to prevent errors
                    else
                        if fluid.amount > 0 then
                            if(global.sendFluids_Buffer[fluid.name] == nil) then
                                global.sendFluids_Buffer[fluid.name] = 0
                            end
                            global.sendFluids_Buffer[fluid.name] = global.sendFluids_Buffer[fluid.name] + fluid.amount
                            send.fluidbox[1] = nil
                        end 
                    end
                end
            end
            for k, send in pairs (global.transferAccumulators) do
                if send.name == "send-accumulator" then
                    if send.energy > 0 then
                        if(global.sendEnergy_Buffer["energy"] == nil) then
                            global.sendEnergy_Buffer["energy"] = 0
                        end
                        global.sendEnergy_Buffer["energy"] = global.sendEnergy_Buffer["energy"] + send.energy
                        send.energy = 0
                    end
                end
            end
        end
    end
)

--[[
    Receive Chest, read from file. Things probably shouldnt be inserted here
]]
remote.add_interface("receive_items",{

    --Insert items, returns the number so items can be adjusted
    inputItems = function(s)
        local itemsGotten = game.json_to_table(s)
        for k,v in pairs(itemsGotten) do
            local itemsToInsert = {name=k, count=v}
            for k, rec in pairs (global.transferChests) do
                if rec.name == "receive-chest" then
                    data = get_chest_data(rec)
                    if data.filter ~= "" then
                        if itemsToInsert.name == data.filter then
                            local inventory = rec.get_inventory(defines.inventory.chest)
                            if inventory.can_insert(itemsToInsert) then
                               itemsToInsert.count = (itemsToInsert.count - inventory.insert(itemsToInsert))
                                if itemsToInsert.count <= 0 then
                                    goto skip_to_next
                                end
                            end
                        else
                            goto skip_to_next
                        end
                    else
                        local inventory = rec.get_inventory(defines.inventory.chest)
                        if inventory.can_insert(itemsToInsert) then
                           itemsToInsert.count = (itemsToInsert.count - inventory.insert(itemsToInsert))
                            if itemsToInsert.count <= 0 then
                                goto skip_to_next
                            end
                        end
                    end
                end
            end
            ::skip_to_next::
        end
    end
})

remote.add_interface("receive_fluids",{
    inputFluids = function(s)
        local fluidsGotten = game.json_to_table(s)
        for k,v in pairs(fluidsGotten) do
            local fluidToInsert = {name=k, amount=v}
            for k, rec in pairs (global.transferTanks) do
                if rec.name == "receive-tank" then
                    data = get_tank_data(rec)
                    if data.filter ~= "" then
                        if fluidToInsert.name == data.filter then
                            fluidToInsert.amount = (fluidToInsert.amount - rec.insert_fluid(fluidToInsert))
                            if fluidToInsert.amount <= 0 then 
                                goto skip_to_next
                            end
                        else
                            goto skip_to_next
                        end
                    else    
                        fluidToInsert.amount = (fluidToInsert.amount - rec.insert_fluid(fluidToInsert))
                        if fluidToInsert.amount <= 0 then 
                            goto skip_to_next
                        end
                    end
                end
            end
            ::skip_to_next::
        end
    end  
})

remote.add_interface("receive_energy",{
    inputEnergy = function(s)
        local energyGotten = game.json_to_table(s)
        for k,v in pairs(energyGotten) do
            local energyToInsert = {name=k, amount=v}
            for k, rec in pairs (global.transferAccumulators) do
                if rec.name == "receive-accumulator" then
                    if (rec.electric_buffer_size - rec.energy) > 0 then
                        energy_inserted = rec.electric_buffer_size - rec.energy
                        energyToInsert.amount = energyToInsert.amount - energy_inserted
                        rec.energy = rec.energy + energy_inserted
                        if energyToInsert.amount <= 0 then 
                            goto skip_to_next
                        end
                    end
                end
            end
            ::skip_to_next::
        end
    end  
})

remote.add_interface("send_items",{
    send_items = function()
        if(global.sendItems == nil) then
            global.sendItems = global.sendItems or {}
        end
        if(global.sendFluids == nil) then
            global.sendFluids = global.sendFluids or {}
        end
        if(global.sendEnergy == nil) then
            global.sendEnergy = global.sendEnergy or {}
        end

        for k ,v in pairs(global.sendItems_Buffer) do
            updateTable = {}
            updateTable["name"] = k
            updateTable["count"] = v
            table.insert(global.sendItems, updateTable)
        end
        
        for k,v in pairs(global.sendFluids_Buffer) do
            updateTable = {}
            updateTable["name"] = k
            updateTable["count"] = math.floor(v)
            table.insert(global.sendFluids, updateTable)
        end

        for k,v in pairs(global.sendEnergy_Buffer) do
            updateTable = {}
            updateTable["name"] = k
            updateTable["count"] = math.floor(v)
            table.insert(global.sendEnergy, updateTable)
        end
        tableToSend = {}
        tableToSend["items"] = global.sendItems
        tableToSend["fluids"] = global.sendFluids
        tableToSend["energy"] = global.sendEnergy

        rcon.print(game.table_to_json(tableToSend))

        global.sendItems = nil
        global.sendItems_Buffer = nil
        global.sendFluids = nil
        global.sendFluids_Buffer = nil
        global.sendEnergy = nil
        global.sendEnergy_Buffer = nil
    end
})