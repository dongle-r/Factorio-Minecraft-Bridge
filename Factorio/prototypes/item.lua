data:extend({
    {
        type = "container",
        name = "send-chest",
        icon = "__transfer-chest__/graphics/send-chest-icon.png",
        icon_size = 32,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 1, result = "send-chest"},
        max_health = 100,
        corpse = "small-remnants",
        open_sound = { filename = "__base__/sound/metallic-chest-open.ogg", volume=0.65 },
        close_sound = { filename = "__base__/sound/metallic-chest-close.ogg", volume = 0.7 },
        collision_box = {{-0.35, -0.35}, {0.35, 0.35}},
        selection_box = {{-0.5, -0.5}, {0.5, 0.5}},
        fast_replaceable_group = "container",
        inventory_size = 1,
        vehicle_impact_sound =  { filename = "__base__/sound/car-metal-impact.ogg", volume = 0.65 },
        picture =
        {
            filename = "__transfer-chest__/graphics/send-chest.png",
            priority = "extra-high",
            width = 48,
            height = 34,
            shift = {0.1875, 0}
        }
    },
    {
        type = "item",
        name = "send-chest",
        icon = "__transfer-chest__/graphics/send-chest-icon.png",
        icon_size = 32,
        flags = {"goes-to-quickbar"},
        subgroup = "storage",
        order = "a[items]-b[send-chest]",
        place_result = "send-chest",
        stack_size = 50
    },
    {
        type = "recipe",
        name = "send-chest",
        ingredients = {{"wood", 2},{"iron-plate", 2}},
        result = "send-chest",
        energy_required = 0.25
    },
    {
        type = "container",
        name = "receive-chest",
        icon = "__transfer-chest__/graphics/receive-chest-icon.png",
        icon_size = 32,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 1, result = "receive-chest"},
        max_health = 100,
        corpse = "small-remnants",
        open_sound = { filename = "__base__/sound/metallic-chest-open.ogg", volume=0.65 },
        close_sound = { filename = "__base__/sound/metallic-chest-close.ogg", volume = 0.7 },
        collision_box = {{-0.35, -0.35}, {0.35, 0.35}},
        selection_box = {{-0.5, -0.5}, {0.5, 0.5}},
        fast_replaceable_group = "container",
        inventory_size = 50,
        vehicle_impact_sound =  { filename = "__base__/sound/car-metal-impact.ogg", volume = 0.65 },
        picture =
        {
            filename = "__transfer-chest__/graphics/receive-chest.png",
            priority = "extra-high",
            width = 48,
            height = 34,
            shift = {0.1875, 0}
        }
    },
    {
        type = "item",
        name = "receive-chest",
        icon = "__transfer-chest__/graphics/receive-chest-icon.png",
        icon_size = 32,
        flags = {"goes-to-quickbar"},
        subgroup = "storage",
        order = "a[items]-b[receive-chest]",
        place_result = "receive-chest",
        stack_size = 50
    },
    {
        type = "recipe",
        name = "receive-chest",
        ingredients = {{"wood", 2},{"iron-plate", 2}},
        result = "receive-chest",
        energy_required = 0.25
    }
})