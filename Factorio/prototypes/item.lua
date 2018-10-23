data:extend({
    {
        type = "container",
        name = "transfer-chest",
        icon = "__transfer-chest__/graphics/transfer-chest-icon.png",
        icon_size = 32,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 1, result = "transfer-chest"},
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
            filename = "__transfer-chest__/graphics/transfer-chest.png",
            priority = "extra-high",
            width = 48,
            height = 34,
            shift = {0.1875, 0}
        }
    },
    {
        type = "item",
        name = "transfer-chest",
        icon = "__transfer-chest__/graphics/transfer-chest-icon.png",
        icon_size = 32,
        flags = {"goes-to-quickbar"},
        subgroup = "storage",
        order = "a[items]-b[transfer-chest]",
        place_result = "transfer-chest",
        stack_size = 50
    },
    {
        type = "recipe",
        name = "transfer-chest",
        ingredients = {{"wood", 2},{"iron-plate", 2}},
        result = "transfer-chest",
        energy_required = 0.25
    }
})