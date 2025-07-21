import React, { useEffect, useState } from 'react';
import { Container, Box, Card, CardContent, Typography, Grid, CardMedia, Button, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const popularBookings = [
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

function AccountPage() {
  const navigate = useNavigate();
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchReservations = async () => {
      setLoading(true);
      setError('');
      try {
        const token = sessionStorage.getItem('jwt');
        const response = await fetch('http://localhost:8080/resmgmtsys/api/reservations', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch reservations');
        }
        const data = await response.json();
        setReservations(data.reservations || []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchReservations();
  }, []);

  if (loading) {
    return (
      <Container maxWidth="md" sx={{ mt: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 200 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
        <Button variant="contained" color="primary" onClick={() => navigate('/')}>Start New Reservation</Button>
      </Box>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {reservations.length > 0 ? (
        <>
          <Typography variant="h5" gutterBottom>My Reservations</Typography>
          <Grid container spacing={2}>
            {reservations.map((res) => (
              <Grid item key={res.id}>
                <Card sx={{ height: '100%' }}>
                  <CardMedia
                    component="img"
                    height="120"
                    image={res.image}
                    alt={res.name}
                  />
                  <CardContent>
                    <Typography variant="subtitle1" fontWeight="bold">
                      {res.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {res.location}
                    </Typography>
                    <Typography variant="body2" sx={{ mt: 1 }}>
                      {res.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </>
      ) : (
        <Card sx={{ mb: 4, boxShadow: 3 }}>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              No bookings under this account
            </Typography>
            <Typography variant="body2" sx={{ mb: 2 }}>
              Here are some popular bookings this month:
            </Typography>
            <Grid container spacing={2}>
              {popularBookings.map((booking) => (
                <Grid item key={booking.id}>
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
      )}
    </Container>
  );
}

export default AccountPage; 