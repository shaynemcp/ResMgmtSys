import React, { useState } from 'react';
import { Container, Box, Card, CardContent, Typography, Grid, CardMedia, AppBar, Toolbar, Button, TextField, InputAdornment, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';

const sampleBookings = [
  {
    id: 1,
    name: 'Lakeview Cabin',
    location: 'Lake Tahoe, CA',
    description: 'A cozy cabin with stunning lake views and modern amenities.',
    image: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80',
  },
  {
    id: 2,
    name: 'Downtown Loft',
    location: 'Austin, TX',
    description: 'Spacious loft in the heart of downtown, perfect for business or leisure.',
    image: 'https://images.unsplash.com/photo-1464983953574-0892a716854b?auto=format&fit=crop&w=400&q=80',
  },
  {
    id: 3,
    name: 'Beachside Bungalow',
    location: 'Miami, FL',
    description: 'Relax in this charming bungalow just steps from the beach.',
    image: 'https://images.unsplash.com/photo-1507089947368-19c1da9775ae?auto=format&fit=crop&w=400&q=80',
  },
  {
    id: 4,
    name: 'Mountain Retreat',
    location: 'Aspen, CO',
    description: 'A luxurious retreat nestled in the mountains, ideal for winter getaways.',
    image: 'https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?auto=format&fit=crop&w=400&q=80',
  },
];

const stateOptions = [
  { code: 'WY', name: 'Wyoming' },
  { code: 'AL', name: 'Alabama' },
  { code: 'HI', name: 'Hawaii' },
  { code: 'NY', name: 'New York' },
  { code: 'MA', name: 'Massachusetts' },
  { code: 'VA', name: 'Virginia' },
  { code: 'FL', name: 'Florida' },
  { code: 'TX', name: 'Texas' },
  { code: 'GA', name: 'Georgia' },
  { code: 'IL', name: 'Illinois' },
  { code: 'WA', name: 'Washington' },
  { code: 'DC', name: 'District of Columbia' },
  { code: 'AZ', name: 'Arizona' },
  { code: 'NV', name: 'Nevada' },
];

function HomePage() {
  const [search, setSearch] = useState('');
  const [selectedState, setSelectedState] = useState('');

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <AppBar position="static" color="default" elevation={1} sx={{ mb: 3 }}>
        <Toolbar>
          <Button color="primary" component={RouterLink} to="/login" sx={{ mr: 2 }}>
            Login
          </Button>
          <Button color="primary" component={RouterLink} to="/register" sx={{ mr: 2 }}>
            Register
          </Button>
        </Toolbar>
      </AppBar>
      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'center' }}>
        <FormControl sx={{ minWidth: 200, mr: 2 }}>
          <InputLabel id="state-select-label">Choose a State</InputLabel>
          <Select
            labelId="state-select-label"
            id="state-select"
            value={selectedState}
            label="Choose a State"
            onChange={e => setSelectedState(e.target.value)}
          >
            <MenuItem value=""><em>All States</em></MenuItem>
            {stateOptions.map((state) => (
              <MenuItem key={state.code} value={state.code}>{state.name}</MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          variant="outlined"
          placeholder="Search for bookings..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
          sx={{ width: '100%', maxWidth: 400 }}
        />
      </Box>
      <Card sx={{ mb: 4, boxShadow: 3 }}>
        <CardContent>
          <Typography variant="h5" component="div" gutterBottom>
            Popular Bookings This Month
          </Typography>
          <Grid container spacing={2}>
            {sampleBookings.map((booking) => (
              <Grid item xs={12} sm={6} md={3} key={booking.id}>
                <Card sx={{ height: '100%' }}>
                  <CardMedia
                    component="img"
                    height="120"
                    image={booking.image}
                    alt={booking.name}
                  />
                  <CardContent>
                    <Typography variant="subtitle1" fontWeight="bold">
                      {booking.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {booking.location}
                    </Typography>
                    <Typography variant="body2" sx={{ mt: 1 }}>
                      {booking.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </CardContent>
      </Card>
    </Container>
  );
}

export default HomePage; 